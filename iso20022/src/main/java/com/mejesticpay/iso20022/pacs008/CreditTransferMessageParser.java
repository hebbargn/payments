package com.mejesticpay.iso20022.pacs008;

import com.mejesticpay.iso20022.base.XMLParser;
import com.mejesticpay.iso20022.type.PartyIdentification135;
import com.mejesticpay.iso20022.type.PaymentTypeInformation28;
import com.mejesticpay.paymentbase.Genesis;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;

public class CreditTransferMessageParser extends XMLParser
{
    private static final Logger logger = LogManager.getLogger(CreditTransferMessageParser.class);

    XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

    public Genesis createGenesis(InputStream inputStream) throws XMLStreamException
    {
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream);
        return createGenesis(xmlStreamReader);
    }

    public Genesis createGenesis(Reader reader) throws XMLStreamException
    {
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(reader);
        return createGenesis(xmlStreamReader);
    }

    private Genesis createGenesis(XMLStreamReader xmlStreamReader) throws XMLStreamException
    {
        Genesis genesis = new Genesis();

        try {
            while (xmlStreamReader.hasNext()) {
                int eventType = xmlStreamReader.next();
//                logger.debug(eventType);
                if (XMLStreamReader.START_ELEMENT == eventType)
                {
                    String localname = xmlStreamReader.getLocalName();
                    if (localname.contains("CreditTransfer"))
                    {
                        logger.debug("got pacs.008");
                        parseCreditTransfer(xmlStreamReader, genesis);
                    }
                }
            }
        }
        finally
        {
            xmlStreamReader.close();
        }
        return genesis;
    }

    private void parseCreditTransfer(XMLStreamReader xmlStreamReader, Genesis genesis) throws XMLStreamException
    {
        while(xmlStreamReader.hasNext())
        {
            int eventType = xmlStreamReader.next();


            if(eventType == XMLStreamReader.START_ELEMENT)
            {

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("PmtId"))
                {
                    logger.debug("Starting PmtId");
                    PaymentTypeInformation28.setPmtId(xmlStreamReader, genesis);
                    continue;
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("IntrBkSttlmAmt"))
                {
                    // Read attribute before reading element text.
                    String currency = xmlStreamReader.getAttributeValue(null,"Ccy");
                    logger.debug(currency);
                    genesis.setSettlementCurrency(currency);

                    String IntrBkSttlmAmt = xmlStreamReader.getElementText();
                    logger.debug(IntrBkSttlmAmt);
                    genesis.setSettlementAmount(new BigDecimal(IntrBkSttlmAmt));
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("InstgAgt"))
                {
                    // TODO: Handle
                    logger.debug("Starting InstgAgt");
                }
                if(xmlStreamReader.getLocalName().equalsIgnoreCase("InstdAgt"))
                {
                    // TODO: Handle
                    logger.debug("Starting InstdAgt");
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("Dbtr"))
                {
                    logger.debug("Starting Debtor");
                    genesis.setDebtor(PartyIdentification135.getParty(xmlStreamReader,"Dbtr"));
                    continue;
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("DbtrAcct"))
                {
                    logger.debug("Starting Debtor Account");
                    genesis.setDebtorAccount(getAccount(xmlStreamReader,"DbtrAcct"));
                    continue;
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("Cdtr"))
                {
                    logger.debug("Starting Creditor");
                    genesis.setCreditor(PartyIdentification135.getParty(xmlStreamReader,"Cdtr"));
                    continue;
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("CdtrAcct"))
                {
                    logger.debug("Starting Creditor Account");
                    genesis.setCreditorAccount(getAccount(xmlStreamReader,"CdtrAcct"));
                    continue;
                }
            }

            //If employee tag is closed then add the employee object to list
            if(eventType == XMLStreamReader.END_ELEMENT)
            {
            }
        }

        logger.debug(genesis);

    }

    private String getAccount(XMLStreamReader xmlStreamReader, String partyIdentifier) throws XMLStreamException
    {
        String accountNumber = null;
        while (xmlStreamReader.hasNext())
        {
            xmlStreamReader.next();
//            printEvent(xmlStreamReader);
            if (xmlStreamReader.getEventType() == XMLStreamReader.CHARACTERS)
            {
                String text = xmlStreamReader.getText();
                if(!StringUtils.isEmpty(StringUtils.trimAllWhitespace(text)))
                {
                    accountNumber = text;
                    logger.debug(accountNumber);
                }
            }

            if (xmlStreamReader.getEventType() == XMLStreamReader.END_ELEMENT)
            {
                if(xmlStreamReader.getLocalName().equalsIgnoreCase(partyIdentifier)) {
                    break;
                }
            }
        }

        return  accountNumber;
    }



}
