package com.mejesticpay.paymentrouter.stp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mejesticpay.commands.STPServiceCommand;
import com.mejesticpay.paymentbase.InFlightTransactionInfo;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentbase.ServiceData;
import com.mejesticpay.paymentbase.ServiceFeed;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.paymentrouter.Station;
import com.mejesticpay.paymentrouter.config.TracksConfig;
import com.mejesticpay.paymentrouter.util.SignHelper;
import com.mejesticpay.service.*;
import com.mejesticpay.stp.STPInboundData;
import com.mejesticpay.util.JSONHelper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.PrivateKey;

@Service
public class DispatcherService
{
    private Logger logger = LoggerFactory.getLogger(DispatcherService.class);
    @Autowired
    private TracksConfig.Tracks tracks;

    @Autowired
    private PrivateKey privateKey;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Value("${CommandStoreURL}")
    private String commandStoreURL;


    RestTemplate restTemplate = new RestTemplate();

    @KafkaListener(topics="${SendToSTPEngine}")
    public void receiveMessage(ConsumerRecord<String,String> record, Acknowledgment acknowledgment)
    {
        logger.info(String.format("Topic - %s, Partition - %d, Value = %s", "SendToSTPEngine", record.partition(), record.value()));

        STPInboundData inboundData = null;
        try
        {
            inboundData = JSONHelper.convertToObjectFromJson(record.value(), STPInboundData.class);
            processFeed(inboundData);
        }catch(Exception e)
        {
            logger.error("",e);
        }

        acknowledgment.acknowledge();

    }

    private void processFeed(STPInboundData inboundData) throws JsonProcessingException
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
            publish(nextStation,payment.getPaymentIdentifier(),payment);

            logger.info("Successfully routed to next station " + nextStation);
        }
        else logger.info("Next Station is not configured for  " + inboundData.getServiceStation());

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
            paymentImpl.setDebitEnrichment((DebitEnrichment)serviceData);
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
        Station station = tracks.getTracksMap().get(trackName).getStations().get(currentStation);
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

    public synchronized void publish(String topic, String key, Payment payment) throws JsonProcessingException
    {
        payment = signPayment(payment);
        String paymentMessage = JSONHelper.convertToStringFromObject(payment);
        kafkaTemplate.send(topic, key, paymentMessage);
    }

    private Payment signPayment(Payment payment)
    {
        try
        {
            PaymentImpl paymentImpl = (PaymentImpl) payment;
            paymentImpl.setSignature(null);
            paymentImpl.setSignature(SignHelper.sign(JSONHelper.convertToStringFromObject(paymentImpl), privateKey));
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
            String newSignature = SignHelper.sign(JSONHelper.convertToStringFromObject(paymentImpl), privateKey);
            return newSignature.equals(currentSignature);
        }catch (Exception e)
        {
            throw new RuntimeException("Failed to sign the message!!!");
        }

    }


}
