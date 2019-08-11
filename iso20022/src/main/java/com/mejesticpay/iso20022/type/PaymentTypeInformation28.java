package com.mejesticpay.iso20022.type;

import com.mejesticpay.iso20022.base.ISOUtil;
import com.mejesticpay.iso20022.base.XMLParser;
import com.mejesticpay.paymentbase.Genesis;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class PaymentTypeInformation28 extends XMLParser
{
    private static final Logger logger = LogManager.getLogger(PaymentTypeInformation28.class);

    public static String PAYMENT_ID = "PmtId";

    // TODO: Create constants for all elements within this tag.

    public static void setPmtId(XMLStreamReader xmlStreamReader, Genesis genesis) throws XMLStreamException
    {
        while (xmlStreamReader.hasNext())
        {
            xmlStreamReader.next();
            //printEvent(xmlStreamReader);
            if(xmlStreamReader.getEventType()  == XMLStreamReader.START_ELEMENT)
            {


                if (xmlStreamReader.getLocalName().equalsIgnoreCase("InstrId")) {
                    String instrId = ISOUtil.randomizeString(xmlStreamReader.getElementText());                    
                    logger.debug(instrId);
                    genesis.setInstructionId(instrId);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("EndToEndId")) {
                    String endToEndId = ISOUtil.randomizeString(xmlStreamReader.getElementText());
                    logger.debug(endToEndId);
                    genesis.setEndToendId(endToEndId);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("TxId")) {
                    String TxId = xmlStreamReader.getElementText();
                    logger.debug(TxId);
                    genesis.setTransactionId(TxId);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("ClrSysRef")) {
                    String ClrSysRef = xmlStreamReader.getElementText();
                    logger.debug(ClrSysRef);
                    genesis.setClearingSystemReference(ClrSysRef);
                }
            }

            if (xmlStreamReader.getEventType() == XMLStreamReader.END_ELEMENT)
            {
                if(xmlStreamReader.getLocalName().equalsIgnoreCase(PAYMENT_ID)) {
                    break;
                }
            }
        }
    }
}
