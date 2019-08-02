package com.mejesticpay.iso20022.pacs008;

import com.mejesticpay.iso20022.base.XMLParser;
import com.mejesticpay.iso20022.type.*;
import com.mejesticpay.paymentbase.Genesis;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("GrpHdr"))
                {
                    logger.error("Starting GrpHdr");
                    GroupHeader93.setGroupHeader(xmlStreamReader, genesis);
                    continue;
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("PmtId")) {
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
                    logger.error("Starting Instructing Agt");
                    genesis.setInstructingAgent(BranchAndFinancialInstitutionIdentification6.getParty(xmlStreamReader, "InstgAgt"));
                }
                if(xmlStreamReader.getLocalName().equalsIgnoreCase("InstdAgt"))
                {
                    logger.error("Starting InstdAgt");
                    genesis.setInstructedAgent(BranchAndFinancialInstitutionIdentification6.getParty(xmlStreamReader, "InstdAgt"));
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
                    genesis.setDebtorAccount(CashAccount38.getAccount(xmlStreamReader,"DbtrAcct"));
                    continue;
                }

                if(xmlStreamReader.getLocalName().equalsIgnoreCase("DbtrAgt"))
                {
                    logger.error("Starting DbtrAgt");
                    genesis.setDebtorAgent(BranchAndFinancialInstitutionIdentification6.getParty(xmlStreamReader, "DbtrAgt"));
                }
                if(xmlStreamReader.getLocalName().equalsIgnoreCase("CdtrAgt"))
                {
                    logger.error("Starting CdtrAgt");
                    genesis.setCreditorAgent(BranchAndFinancialInstitutionIdentification6.getParty(xmlStreamReader, "CdtrAgt"));
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
                    genesis.setCreditorAccount(CashAccount38.getAccount(xmlStreamReader,"CdtrAcct"));
                    continue;
                }

            }  // End of START_ELEMENT


            if(eventType == XMLStreamReader.END_ELEMENT) {// TODO }
        }


        }
        logger.error(genesis);
    }

}
