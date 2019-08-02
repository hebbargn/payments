package com.mejesticpay.iso20022.type;

import com.mejesticpay.iso20022.base.XMLParser;
import com.mejesticpay.paymentbase.Party;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class PartyIdentification135 extends XMLParser
{
    private static final Logger logger = LogManager.getLogger(PartyIdentification135.class);

    public static Party getParty(XMLStreamReader xmlStreamReader, String partyIdentifier) throws XMLStreamException
    {
        Party party = new Party();

        while (xmlStreamReader.hasNext())
        {
            xmlStreamReader.next();

            //printEvent(xmlStreamReader);

            if (xmlStreamReader.getEventType() == XMLStreamReader.START_ELEMENT)
            {
                if (xmlStreamReader.getLocalName().equalsIgnoreCase("Nm")) {
                    String name = xmlStreamReader.getElementText();
                    logger.debug(name);
                    party.setName(name);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("StrtNm")) {
                    String StrtNm = xmlStreamReader.getElementText();
                    logger.debug(StrtNm);
                    party.setStreet(StrtNm);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("BldgNb")) {
                    String BldgNb = xmlStreamReader.getElementText();
                    logger.debug(BldgNb);
                    party.setBuildingNumber(BldgNb);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("PstCd")) {
                    String PstCd = xmlStreamReader.getElementText();
                    logger.debug(PstCd);
                    party.setPostalCode(PstCd);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("TwnNm")) {
                    String TwnNm = xmlStreamReader.getElementText();
                    logger.debug(TwnNm);
                    party.setTownName(TwnNm);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("Ctry")) {
                    String Ctry = xmlStreamReader.getElementText();
                    logger.debug(Ctry);
                    party.setCountry(Ctry);
                }
            }

            if (xmlStreamReader.getEventType() == XMLStreamReader.END_ELEMENT)
            {
                if(xmlStreamReader.getLocalName().equalsIgnoreCase(partyIdentifier)) {
                    logger.debug("Breaking out of loop...");
                    return party;
                }
            }
        }
        return  party;
    }

}
