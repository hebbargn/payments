package com.mejesticpay.rtpgateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mejesticpay.iso20022.pacs008.CreditTransferMessageParser;
import com.mejesticpay.paymentbase.*;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.rtpgateway.mysql.DuplicateCheck;
import com.mejesticpay.rtpgateway.mysql.DuplicateCheckRepository;
import com.mejesticpay.service.RoutePayment;
import com.mejesticpay.util.JSONHelper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.UUID;


@Service
public class InboundCreditTransferOperation {

    private static final Logger logger = LogManager.getLogger(InboundCreditTransferOperation.class);

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    @Qualifier("CustomDuplicateCheck")
    private DuplicateCheckRepository duplicateCheckRepository;

    RestTemplate restTemplate = new RestTemplate();

    @Value("${PaymentStoreURL}")
    private String paymentStoreURL;

    @Value("${SendToSTPEngine}")
    private String sendToSTPEngine;

    @KafkaListener(topics="RTP_Inbound_CT_v06")
    public void receiveMessage(ConsumerRecord<String,String> record, Acknowledgment acknowledgment)
    {
        logger.debug(String.format("Topic - %s, Partition - %d, Value = %s", "RoutePayment", record.partition(), record.value()));
        Reader reader = new StringReader(record.value());

        try {
            Genesis genesis = new CreditTransferMessageParser().createGenesis(reader);

            // Perform duplicate check
            performDuplicateCheck(genesis);

            // TODO: SLA check
            // TODO: If above checks fail, sends pacs.002 rejection back by creating Distribution object.

            // Convert to Payment object
            Payment payment = generatePayment(genesis);

            // Save Payment in database
            HttpEntity<Payment> request = new HttpEntity(payment);
            ResponseEntity<PaymentImpl> response = restTemplate.exchange(paymentStoreURL, HttpMethod.POST,request,PaymentImpl.class);

            logger.debug("Successfully created payment");

            ServiceFeed feed = new ServiceFeed(new InFlightTransactionInfo(payment,"New"), new RoutePayment());
            feed.setResult(ServiceFeed.PROCESSING_RESULT.SUCCESS);

            // Send it to STP router
            kafkaTemplate.send(sendToSTPEngine, payment.getPaymentIdentifier(), JSONHelper.convertToStringFromObject(feed));


        } catch (XMLStreamException | JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        acknowledgment.acknowledge();

    }

    public void performDuplicateCheck(Genesis genesis)
    {
        DuplicateCheck duplicateCheck = new DuplicateCheck();
        duplicateCheck.setInstruction_id(genesis.getInstructionId());
        try {
            duplicateCheckRepository.save(duplicateCheck);
        } catch (DuplicateKeyException e)
        {
            // TODO: Handle duplicate check
            logger.error(e.getMessage(), e);
        }
    }

    public Payment generatePayment(Genesis origination)
    {
        PaymentImpl payment = new PaymentImpl(origination);
        payment.incrementVersion();
        payment.setState("Active");
        payment.setSource("ISO");
        payment.setBranch("US");

        payment.setTrack("OutboundPayment");
        payment.setStation("New");

        payment.setStatus("Ready");

        payment.setPaymentIdentifier(UUID.randomUUID().toString());

        //TODO - Cleanup the audit here
        payment.addAuditEntry(new AuditEntry("InboundCreditTransferOperation","Successfully create payment from PACS.008",null));
        return payment;
    }
}
