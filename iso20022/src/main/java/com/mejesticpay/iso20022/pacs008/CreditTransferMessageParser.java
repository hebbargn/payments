package com.mejesticpay.iso20022.pacs008;

import com.mejesticpay.iso20022.base.XMLParser;
import com.mejesticpay.iso20022.type.PartyIdentification135;
import com.mejesticpay.iso20022.type.PaymentTypeInformation28;
import com.mejesticpay.paymentbase.Genesis;
import org.springframework.util.StringUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;

public class CreditTransferMessageParser extends XMLParser
{
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
//                System.out.println(eventType);
                if (XMLStreamReader.START_ELEMENT == eventType)
                {
                    String localname = xmlStreamReader.getLocalName();
                    if (localname.contains("CreditTransfer"))
                    {
                        System.out.println("got pacs.008");
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
                    System.out.println("Starting PmtId");
                    PaymentTypeInformation28.setPmtId(xmlStreamReader, genesis);
                    continue;
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("IntrBkSttlmAmt"))
                {
                    // Read attribute before reading element text.
                    String currency = xmlStreamReader.getAttributeValue(null,"Ccy");
                    System.out.println(currency);
                    genesis.setSettlementCurrency(currency);

                    String IntrBkSttlmAmt = xmlStreamReader.getElementText();
                    System.out.println(IntrBkSttlmAmt);
                    genesis.setSettlementAmount(new BigDecimal(IntrBkSttlmAmt));
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("InstgAgt"))
                {
                    // TODO: Handle
                    System.out.println("Starting InstgAgt");
                }
                if(xmlStreamReader.getLocalName().equalsIgnoreCase("InstdAgt"))
                {
                    // TODO: Handle
                    System.out.println("Starting InstdAgt");
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("Dbtr"))
                {
                    System.out.println("Starting Debtor");
                    genesis.setDebtor(PartyIdentification135.getParty(xmlStreamReader,"Dbtr"));
                    continue;
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("DbtrAcct"))
                {
                    System.out.println("Starting Debtor Account");
                    genesis.setDebtorAccount(getAccount(xmlStreamReader,"DbtrAcct"));
                    continue;
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("Cdtr"))
                {
                    System.out.println("Starting Creditor");
                    genesis.setCreditor(PartyIdentification135.getParty(xmlStreamReader,"Cdtr"));
                    continue;
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("CdtrAcct"))
                {
                    System.out.println("Starting Creditor Account");
                    genesis.setCreditorAccount(getAccount(xmlStreamReader,"CdtrAcct"));
                    continue;
                }
            }

            //If employee tag is closed then add the employee object to list
            if(eventType == XMLStreamReader.END_ELEMENT)
            {
            }
        }

        System.out.println(genesis);

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
                    System.out.println(accountNumber);
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
