package com.mejesticpay.debitenrichment;

import com.mejesticpay.paymentbase.*;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.service.DebitEnrichment;
import com.mejesticpay.util.JSONHelper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;

@SpringBootApplication
public class PaymentDebitEnricher
{
    private Logger logger = LoggerFactory.getLogger(PaymentDebitEnricher.class);

    public static void main(String []args)
    {
            SpringApplication.run(PaymentDebitEnricher.class,args);
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @KafkaListener(topics = "DebitEnrichment")
    public void receiveMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try
        {
            logger.info(String.format("Topic - %s, Partition - %d, Value = %s", "DebitEnrichment", record.partition(), record.value()));

            Payment payment = JSONHelper.convertToObjectFromJson(record.value(), PaymentImpl.class);

            DebitEnrichment debitEnrich = new DebitEnrichment();
            Party debitPatry = new Party();
            debitPatry.setName((payment.getGenesis().getDebtor().getName()));
            debitPatry.setAccounts(payment.getGenesis().getDebtor().getAccounts());
            debitEnrich.setDebitParty(debitPatry);

            AuditEntry entry = new AuditEntry(PaymentDebitEnricher.class.getName(), "Successfully enriched Debit party", null);

            ServiceFeed feed = new ServiceFeed(new InFlightTransactionInfo(payment, "DebitEnrichment"), debitEnrich);
            feed.addAuditEntry(entry);
            feed.setResult(ServiceFeed.PROCESSING_RESULT.SUCCESS);

            kafkaTemplate.send("PaymentRouter", payment.getPaymentIdentifier(), JSONHelper.convertToStringFromObject(feed));
            logger.info(JSONHelper.convertToPrettyStringFromObject(feed));
            logger.info("Successfully processed debit enrichment");

        } catch (Exception e)
        {
            logger.error("Exception ", e);
        }

        acknowledgment.acknowledge();


    }
}