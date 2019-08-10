package com.mejesticpay.enrichment.debit;

import com.mejesticpay.enrichment.credit.InboundCTCreditEnrichment;
import com.mejesticpay.enrichment.util.ClearingAccountLookup;
import com.mejesticpay.enrichment.util.PartyLookupRequest;
import com.mejesticpay.enrichment.util.PartyLookupResponse;
import com.mejesticpay.paymentbase.*;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.service.DebitEnrichment;
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

@Service
public class InboundCTDebitEnrichment
{
    private Logger logger = LoggerFactory.getLogger(InboundCTCreditEnrichment.class);


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${InboundCTDebitEnrichment:InboundCTDebitEnrichment}")
    public final String serviceName ="InboundCTDebitEnrichment";

    @Value("${SendPaymentToSTPEngine:SendToSTPEngine}")
    private String SendPaymentToSTPEngine;

    @KafkaListener(topics = serviceName)
    public void receiveMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try
        {
            logger.info(String.format("Topic - %s, Partition - %d, Value = %s", serviceName, record.partition(), record.value()));

            Payment payment = JSONHelper.convertToObjectFromJson(record.value(), PaymentImpl.class);
            ServiceFeed serviceFeed = new ServiceFeed(new InFlightTransactionInfo(payment,serviceName));

            DebitEnrichment debitEnrich = new DebitEnrichment();
            PartyLookupRequest request = new PartyLookupRequest(PartyLookupRequest.LookupType.CLEARING);
            request.setClearing("RTP");

            PartyLookupResponse result = new ClearingAccountLookup().lookup(request);
            if(result.getResult() == PartyLookupResponse.LookupResult.SUCCESS)
            {
                Party debitParty = new Party();
                result.applyToParty(debitParty);
                debitEnrich.setDebitParty(debitParty);
                serviceFeed.setServiceData(debitEnrich);

                String auditMessage = "Successfully enriched debit party from Clearing Account.";
                String detailsJSON = JSONHelper.convertToStringFromObject(result);
                AuditEntry auditEntry = new AuditEntry(serviceName,auditMessage,detailsJSON);
                serviceFeed.addAuditEntry(auditEntry);

                serviceFeed.setResult(ServiceFeed.PROCESSING_RESULT.SUCCESS);
            }
            else
            {
                AuditEntry auditEntry = new AuditEntry(serviceName,"Failed to enrich from Clearing Account",null);
                serviceFeed.addAuditEntry(auditEntry);
                serviceFeed.setResult(ServiceFeed.PROCESSING_RESULT.FAILURE);
            }

            kafkaTemplate.send(SendPaymentToSTPEngine, payment.getPaymentIdentifier(), JSONHelper.convertToStringFromObject(serviceFeed));
            logger.info(JSONHelper.convertToPrettyStringFromObject(serviceFeed));
            logger.info("Successfully processed inbound CT Debit enrichment");

        } catch (Exception e)
        {
            logger.error("Exception ", e);
        }

        acknowledgment.acknowledge();


    }
}
