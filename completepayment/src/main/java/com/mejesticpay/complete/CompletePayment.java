package com.mejesticpay.complete;

import com.mejesticpay.paymentbase.AuditEntry;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.util.JSONHelper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CompletePayment {
    private Logger logger = LoggerFactory.getLogger(CompletePayment.class);
    RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(CompletePayment.class, args);
    }


    @KafkaListener(topics = "Complete")
    public void receiveMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try
        {
            logger.info(String.format("Topic - %s, Partition - %d, Value = %s", "CompletePayment", record.partition(), record.value()));

            PaymentImpl payment = JSONHelper.convertToObjectFromJson(record.value(), PaymentImpl.class);

            payment.setStation("Complete");
            AuditEntry entry = new AuditEntry( CompletePayment.class.getName(),"Successfully completed the payment", null);
            payment.addAuditEntry(entry);
            payment.incrementVersion();

            HttpEntity<Payment> request = new HttpEntity(payment);
            ResponseEntity<PaymentImpl> response = restTemplate.exchange("http://localhost:8080/payments/"+payment.getPaymentIdentifier(), HttpMethod.PUT,request,PaymentImpl.class);

            logger.info("Successfully completed the payment");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        acknowledgment.acknowledge();
    }
}
