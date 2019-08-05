package com.mejesticpay.rtpgateway.controller;

import com.mejesticpay.iso20022.base.ISOUtil;
import com.mejesticpay.iso20022.pacs008.CreditTransferMessageParser;
import com.mejesticpay.paymentbase.Genesis;
import com.mejesticpay.rtpgateway.mysql.CollectionPoint;
import com.mejesticpay.rtpgateway.mysql.CollectionPointRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDateTime;

@RestController
public class InboundOperation
{
    private static final Logger logger = LogManager.getLogger(InboundOperation.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private CollectionPointRepository collectionPointRepository;


    @RequestMapping("/ping")
    public String test() {
        return "pong " + (int) (Math.random() * 10000);
    }

    @PostMapping("/process")
    public String processMessage(@RequestBody String inputMessage) throws IOException
    {
        if (logger.isDebugEnabled())
            logger.debug("Received : " + inputMessage);

        String msgType = null;

        try {
            msgType = ISOUtil.getMessageType(inputMessage);
            logger.debug("Received message type: " + msgType);
        } catch (XMLStreamException e) {
            logger.error(e.getMessage(), e);
        }

        if ("pacs.008.001.06".equals(msgType)) {
            logger.debug("Processing " + msgType + " message.");

            // TODO: Store message in database.
            CollectionPoint cp = new CollectionPoint();
            cp.setMessage_text(inputMessage);
            cp.setMessage_type("pacs.008.001.06");
            cp.setCreation_time(LocalDateTime.now());
            // TODO: Set other values

            collectionPointRepository.save(cp);

            // Perform Schema validation based on configuration.
//            XmlValidationUtil.validate(inputMessage, "xsd location");

            // Send it to Kafka topic
            kafkaTemplate.send("RTP_Inbound_CT_v06", String.valueOf(cp.getId()), inputMessage);

        }

        return "Message Processed successfully ..." + (int) (Math.random() * 10000);
    }


    @PostMapping("/payment")
    public String createPayment(@RequestBody String pacs008) throws IOException {
        logger.debug("Received : " + pacs008);
        Reader reader = new StringReader(pacs008);

        try {
            Genesis genesis = new CreditTransferMessageParser().createGenesis(reader);


        } catch (XMLStreamException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }

        return "Payment created 234...";
    }
}

