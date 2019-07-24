package com.mejesticpay.iso20022.type;

import com.mejesticpay.paymentbase.Party;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class PartyIdentification135
{
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
                    System.out.println(name);
                    party.setName(name);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("StrtNm")) {
                    String StrtNm = xmlStreamReader.getElementText();
                    System.out.println(StrtNm);
                    party.setStreet(StrtNm);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("BldgNb")) {
                    String BldgNb = xmlStreamReader.getElementText();
                    System.out.println(BldgNb);
                    party.setBuildingNumber(BldgNb);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("PstCd")) {
                    String PstCd = xmlStreamReader.getElementText();
                    System.out.println(PstCd);
                    party.setPostalCode(PstCd);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("TwnNm")) {
                    String TwnNm = xmlStreamReader.getElementText();
                    System.out.println(TwnNm);
                    party.setTownName(TwnNm);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("Ctry")) {
                    String Ctry = xmlStreamReader.getElementText();
                    System.out.println(Ctry);
                    party.setCountry(Ctry);
                }
            }

            if (xmlStreamReader.getEventType() == XMLStreamReader.END_ELEMENT)
            {
                if(xmlStreamReader.getLocalName().equalsIgnoreCase(partyIdentifier)) {
                    System.out.println("Breaking out of loop...");
                    return party;
                }
            }
        }
        return  party;
    }

}
