package com.mejesticpay.enrichment.credit;

import com.mejesticpay.enrichment.util.CIFLookup;
import com.mejesticpay.enrichment.util.PartyLookupRequest;
import com.mejesticpay.enrichment.util.PartyLookupResponse;
import com.mejesticpay.paymentbase.*;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.service.CreditEnrichment;
import com.mejesticpay.util.JSONHelper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InboundCTCreditEnrichment
{
    private Logger logger = LoggerFactory.getLogger(InboundCTCreditEnrichment.class);


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${InboundCTCreditEnrichment:InboundCTCreditEnrichment}")
    public final String serviceName ="InboundCTCreditEnrichment";

    @Value("${SendPaymentToSTPEngine:SendToSTPEngine}")
    private String SendPaymentToSTPEngine;

    @Value("${CIFRestUrl}")
    private String cifRestURL;

    @KafkaListener(topics = serviceName)
    public void receiveMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try
        {
            logger.info(String.format("Topic - %s, Partition - %d, Value = %s", serviceName, record.partition(), record.value()));

            Payment payment = JSONHelper.convertToObjectFromJson(record.value(), PaymentImpl.class);
            ServiceFeed serviceFeed = new ServiceFeed(new InFlightTransactionInfo(payment,serviceName));

            CreditEnrichment creditEnrich = new CreditEnrichment();
            PartyLookupRequest request = new PartyLookupRequest(PartyLookupRequest.LookupType.CIF);
            request.setCifRequestURL(cifRestURL);
            request.setParty(payment.getGenesis().getCreditor());
            request.setCifAccount(payment.getGenesis().getCreditorAccount());
            PartyLookupResponse result = new CIFLookup().lookup(request);
            if(result.getResult() == PartyLookupResponse.LookupResult.SUCCESS)
            {
                Party creditParty = new Party();
                result.applyToParty(creditParty);

                creditEnrich.setCreditor(creditParty);
                creditEnrich.setSettlementDate(LocalDate.now());
                creditEnrich.setClearingDate(LocalDate.now());
                serviceFeed.setServiceData(creditEnrich);

                String auditMessage = "Successfully enriched debit party from CIF.";
                String detailsJSON = JSONHelper.convertToStringFromObject(result);
                AuditEntry auditEntry = new AuditEntry(serviceName,auditMessage,detailsJSON);
                serviceFeed.addAuditEntry(auditEntry);

                serviceFeed.setResult(ServiceFeed.PROCESSING_RESULT.SUCCESS);
            }
            else
            {
                AuditEntry auditEntry = new AuditEntry(serviceName,"Failed to enrich from CIF",null);
                serviceFeed.addAuditEntry(auditEntry);
                serviceFeed.setResult(ServiceFeed.PROCESSING_RESULT.FAILURE);
            }

            kafkaTemplate.send(SendPaymentToSTPEngine, payment.getPaymentIdentifier(), JSONHelper.convertToStringFromObject(serviceFeed));
            logger.info(JSONHelper.convertToPrettyStringFromObject(serviceFeed));
            logger.info("Successfully processed inbound CT enrichment");

        } catch (Exception e)
        {
            logger.error("Exception ", e);
        }

        acknowledgment.acknowledge();


    }
}
