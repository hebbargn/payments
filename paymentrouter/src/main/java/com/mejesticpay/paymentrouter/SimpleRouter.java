package com.mejesticpay.paymentrouter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mejesticpay.paymentbase.AuditEntry;
import com.mejesticpay.paymentbase.InFlightTransactionInfo;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentbase.ServiceFeed;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.paymentrouter.config.TracksConfig;
import com.mejesticpay.service.*;
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

@Service
public class SimpleRouter
{
    private Logger logger = LoggerFactory.getLogger(SimpleRouter.class);
    @Autowired
    private TracksConfig.Tracks tracks;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    RestTemplate restTemplate = new RestTemplate();


    @Value("${PaymentStoreURL}")
    private String paymentStoreURL;

    @KafkaListener(topics="${SendToSTPEngine}")
    public void receiveMessage(ConsumerRecord<String,String>record, Acknowledgment acknowledgment)
    {
        logger.info(String.format("Topic - %s, Partition - %d, Value = %s", "SendToSTPEngine", record.partition(), record.value()));

        ServiceFeed feed = null;
        try
        {
            feed = JSONHelper.convertToObjectFromJson(record.value(), ServiceFeed.class);
            routeNext(feed);
        }catch(Exception e)
        {
            logger.error("",e);
        }

        acknowledgment.acknowledge();

    }

    private void routeNext(ServiceFeed serviceFeed) throws JsonProcessingException
    {
        InFlightTransactionInfo flight = serviceFeed.getInFlightTransactionInfo();

        ResponseEntity<PaymentImpl> result = restTemplate.getForEntity(paymentStoreURL+flight.getPaymentIdentifier(),PaymentImpl.class);
        Payment payment = result.getBody();

        String nextStation = getNextStation(serviceFeed,payment.getTrack(), flight.getCurrentStation());

        if(!(serviceFeed.getServiceData() instanceof RoutePayment))
        {
            updatePaymentFromServiceFeed(payment,serviceFeed,nextStation);
        }

        if(nextStation != null)
        {
            publish(nextStation,payment.getPaymentIdentifier(),JSONHelper.convertToStringFromObject(payment));

            logger.info("Successfully routed to next station " + nextStation);
        }
        else logger.info("Next Station is not configured for  " + flight.getCurrentStation());

    }

    private Payment updatePaymentFromServiceFeed(Payment payment, ServiceFeed serviceFeed, String nextStation)throws JsonProcessingException
    {
        PaymentImpl paymentImpl = (PaymentImpl) payment;
        if (serviceFeed.getServiceData() instanceof DebitEnrichment) {
            paymentImpl.setDebitEnrichment((DebitEnrichment) serviceFeed.getServiceData());
        } else if (serviceFeed.getServiceData() instanceof SanctionsCheckInfo) {
            paymentImpl.setSanctionsCheckInfo((SanctionsCheckInfo) serviceFeed.getServiceData());
        } else if (serviceFeed.getServiceData() instanceof FraudCheckInfo) {
            paymentImpl.setFraudCheckInfo((FraudCheckInfo) serviceFeed.getServiceData());
        } else if (serviceFeed.getServiceData() instanceof CreditEnrichment) {
            paymentImpl.setCreditEnrichment((CreditEnrichment) serviceFeed.getServiceData());
        } else {
            logger.error("No update made to the payment");
            return payment;
        }

        paymentImpl.setStation(nextStation);
        paymentImpl.addAuditEntries(serviceFeed.getAuditEntries());

        AuditEntry entry = new AuditEntry( SimpleRouter.class.getName(),serviceFeed.getInFlightTransactionInfo().getCurrentStation()+" Service Data",
                JSONHelper.convertToStringFromObject(serviceFeed.getServiceData()));
        paymentImpl.addAuditEntry(entry);

        HttpEntity<Payment> request = new HttpEntity(paymentImpl);
        ResponseEntity<PaymentImpl> response = restTemplate.exchange(paymentStoreURL+payment.getPaymentIdentifier(), HttpMethod.PUT,request,PaymentImpl.class);
        return response.getBody();


    }

    private String getNextStation(ServiceFeed feed, String trackName, String currentStation)
    {
        Station station = tracks.getTracksMap().get(trackName).getStations().get(currentStation);
        if(station != null)
        {
            switch (feed.getResult())
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

    public synchronized void publish(String topic, String key, String message)
    {
        kafkaTemplate.send(topic,key,message);
    }
}
