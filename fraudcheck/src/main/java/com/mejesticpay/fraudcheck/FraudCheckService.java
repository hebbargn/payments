package com.mejesticpay.fraudcheck;

import com.mejesticpay.paymentbase.*;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.service.DebitEnrichment;
import com.mejesticpay.service.FraudCheckInfo;
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

import java.time.LocalDate;

@SpringBootApplication
public class FraudCheckService
{
    private Logger logger = LoggerFactory.getLogger(FraudCheckService.class);

    public static void main(String[] args)
    {
        SpringApplication.run(FraudCheckService.class, args);
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @KafkaListener(topics = "FraudCheckService")
    public void receiveMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment)
    {
        try
        {
            logger.info(String.format("Topic - %s, Partition - %d, Value = %s", "FraudCheckService", record.partition(), record.value()));

            Payment payment = JSONHelper.convertToObjectFromJson(record.value(), PaymentImpl.class);

            FraudCheckInfo fraud = new FraudCheckInfo();
            fraud.setFraudCheckStatus("Approved");
            fraud.setFraudCheckDate(LocalDate.now());


            AuditEntry entry = new AuditEntry(FraudCheckService.class.getName(), "Successfully completed the FraudCheck", null);

            ServiceFeed feed = new ServiceFeed(new InFlightTransactionInfo(payment, "FraudCheckService"), fraud);
            feed.addAuditEntry(entry);
            feed.setResult(ServiceFeed.PROCESSING_RESULT.SUCCESS);

            kafkaTemplate.send("PaymentRouter", payment.getPaymentIdentifier(), JSONHelper.convertToStringFromObject(feed));
            logger.info(JSONHelper.convertToPrettyStringFromObject(feed));
            logger.info("Successfully processed FraudCheckService");

        }catch (Exception e)
        {

        }

        acknowledgment.acknowledge();


    }
}
