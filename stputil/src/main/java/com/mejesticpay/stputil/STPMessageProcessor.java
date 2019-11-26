package com.mejesticpay.stputil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mejesticpay.commands.STPServiceCommand;
import com.mejesticpay.paymentbase.InFlightTransactionInfo;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentbase.ServiceData;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.service.CreditEnrichment;
import com.mejesticpay.service.DebitEnrichment;
import com.mejesticpay.service.FraudCheckInfo;
import com.mejesticpay.service.RoutePayment;
import com.mejesticpay.stp.STPInboundData;
import com.mejesticpay.stputil.config.KeyConfig;
import com.mejesticpay.stputil.config.TracksConfig;
import com.mejesticpay.util.JSONHelper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class STPMessageProcessor
{

    private RestTemplate restTemplate;
    private String commandStoreURL;
    public STPMessageProcessor(RestTemplate restTemplate,String commandStoreURL )
    {
        this.restTemplate = restTemplate;
        this.commandStoreURL = commandStoreURL;
    }
    public STPProcessingResult processInboundData(STPInboundData inboundData) throws JsonProcessingException
    {
        Payment payment = inboundData.getPayment();
        String nextStation = getNextStation(inboundData.getResult(),payment.getTrack(), inboundData.getServiceStation());

        if(!(inboundData.getServiceData() instanceof RoutePayment))
        {
            InFlightTransactionInfo updatedTransactionInfo =  sendPaymentUpdateCommand(inboundData);
            payment =  mergePayment( payment,  inboundData,  updatedTransactionInfo);
        }

        if(nextStation != null)
        {
            STPProcessingResult result = new STPProcessingResult(true);
            result.setNextStation(nextStation);
            result.setPayment(signPayment(payment));
            System.out.println("Next station is  " + nextStation);
            return result;
        }
        else
        {
            System.out.println("Next Station is not configured for  " + inboundData.getServiceStation());
            return new STPProcessingResult(false);
        }
    }


    private InFlightTransactionInfo sendPaymentUpdateCommand(STPInboundData inboundData )
    {
        ServiceData serviceData = inboundData.getServiceData();
        STPServiceCommand.ServiceType serviceType;
        if (serviceData instanceof DebitEnrichment)
        {
            serviceType = STPServiceCommand.ServiceType.DebitEnrichment;
        } else if (serviceData instanceof CreditEnrichment)
        {
            serviceType = STPServiceCommand.ServiceType.CreditEnrichment;
        } else if (serviceData instanceof FraudCheckInfo) {
            serviceType = STPServiceCommand.ServiceType.FraudCheckInfo;
        }
        else
        {
            throw new RuntimeException("Invalid ServiceType");
        }

        STPServiceCommand command = new STPServiceCommand(serviceType,serviceData,new InFlightTransactionInfo(inboundData.getPayment(),inboundData.getServiceStation()));

        HttpEntity<STPServiceCommand> request = new HttpEntity(command);
        ResponseEntity<InFlightTransactionInfo> response = restTemplate.exchange(commandStoreURL, HttpMethod.POST,request,InFlightTransactionInfo.class);
        return response.getBody();

    }

    private Payment mergePayment(Payment payment, STPInboundData inboundData,  InFlightTransactionInfo newTransactionInfo)
    {
        PaymentImpl paymentImpl = (PaymentImpl)payment;
        paymentImpl.setVersion(newTransactionInfo.getVersion());
        paymentImpl.setStation(newTransactionInfo.getCurrentStation());
        paymentImpl.setState(newTransactionInfo.getState());
        ServiceData serviceData = inboundData.getServiceData();
        if (serviceData instanceof DebitEnrichment)
        {
            paymentImpl.setDebitEnrichment((DebitEnrichment)serviceData);
        } else if (serviceData instanceof CreditEnrichment)
        {
            paymentImpl.setCreditEnrichment((CreditEnrichment)serviceData);
        } else if (serviceData instanceof FraudCheckInfo)
        {
            paymentImpl.setFraudCheckInfo((FraudCheckInfo)serviceData);
        } else
        {
            throw new RuntimeException("Invalid serviceData");
        }

        return paymentImpl;

    }
    private String getNextStation(STPInboundData.PROCESSING_RESULT result, String trackName, String currentStation)
    {
        Station station = TracksConfig.getTracksMap().get(trackName).getStations().get(currentStation);
        if(station != null)
        {
            switch (result)
            {
                case SUCCESS:
                    return station.getOnSuccess();
                case FAILURE:
                    return station.getOnFailure();
                case HOLD:
                    return station.getOnHold();
                case STATECHANGE:
                    return station.getOnStateChange();
            }
        }

        return null;
    }


    private Payment signPayment(Payment payment)
    {
        try
        {
            PaymentImpl paymentImpl = (PaymentImpl) payment;
            paymentImpl.setSignature(null);
            paymentImpl.setSignature(SignHelper.sign(JSONHelper.convertToStringFromObject(paymentImpl), KeyConfig.getPrivateKey()));
            return payment;
        }catch (Exception e)
        {
            throw new RuntimeException("Failed to sign the message!!!");
        }
    }

    private boolean validateSignature(Payment payment)
    {
        try
        {
            PaymentImpl paymentImpl = (PaymentImpl)payment;

            String currentSignature = paymentImpl.getSignature();
            paymentImpl.setSignature(null);
            String newSignature = SignHelper.sign(JSONHelper.convertToStringFromObject(paymentImpl), KeyConfig.getPrivateKey());
            return newSignature.equals(currentSignature);
        }catch (Exception e)
        {
            throw new RuntimeException("Failed to sign the message!!!");
        }

    }
}
