package com.mejesticpay.iso20022.type;

import com.mejesticpay.iso20022.base.XMLParser;
import com.mejesticpay.paymentbase.Genesis;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalDate;

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
//            printEvent(xmlStreamReader);
            if(xmlStreamReader.getEventType()  == XMLStreamReader.START_ELEMENT)
            {
                if (xmlStreamReader.getLocalName().equalsIgnoreCase("IntrBkSttlmDt")) {
                    // 2019-07-23
                    String dateAsString = xmlStreamReader.getElementText();
                    System.out.println(dateAsString);
                    LocalDate localDate = LocalDate.parse(dateAsString);
                    //Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    genesis.setSettlementDate(localDate);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("InstrId")) {
                    String InstrId = xmlStreamReader.getElementText();
                    System.out.println(InstrId);
                    genesis.setInstructionId(InstrId);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("EndToEndId")) {
                    String EndToEndId = xmlStreamReader.getElementText();
                    System.out.println(EndToEndId);
                    genesis.setEndToendId(EndToEndId);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("TxId")) {
                    String TxId = xmlStreamReader.getElementText();
                    System.out.println(TxId);
                    genesis.setTransactionId(TxId);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("ClrSysRef")) {
                    String ClrSysRef = xmlStreamReader.getElementText();
                    System.out.println(ClrSysRef);
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
