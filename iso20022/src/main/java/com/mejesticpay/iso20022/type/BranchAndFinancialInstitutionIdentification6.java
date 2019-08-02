package com.mejesticpay.iso20022.type;

import com.mejesticpay.iso20022.base.XMLParser;
import com.mejesticpay.paymentbase.BranchAndFinancialInstitution;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

@Setter
@Getter
public class BranchAndFinancialInstitutionIdentification6 extends XMLParser
{
    private static final Logger logger = LogManager.getLogger(BranchAndFinancialInstitutionIdentification6.class);

    private String BICFI;
    private String ClrSysMmbId;
    private String name;
    private PostalAddress24 address;
    private String otherId;


    public static BranchAndFinancialInstitution getParty(XMLStreamReader xmlStreamReader, String partyIdentifier) throws XMLStreamException
    {
        BranchAndFinancialInstitution party = new BranchAndFinancialInstitution();

        while (xmlStreamReader.hasNext())
        {
            xmlStreamReader.next();

            //printEvent(xmlStreamReader);

            if (xmlStreamReader.getEventType() == XMLStreamReader.START_ELEMENT)
            {
                if (xmlStreamReader.getLocalName().equalsIgnoreCase("BICFI")) {
                    String name = xmlStreamReader.getElementText();
                    logger.debug(name);
                    // TODO: handle BICFI
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("ClrSysMmbId")) {
                    String memId = getClrSysMmbId(xmlStreamReader, "ClrSysMmbId");
                    logger.debug(memId);
                    party.setClrSysMmbId (memId);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("Nm")) {
                    String name = xmlStreamReader.getElementText();
                    logger.debug(name);
                    party.setName(name);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("PstlAdr")) {
//                    String PstCd = xmlStreamReader.getElementText();
//                    logger.debug(PstCd);
//                    party.setPostalCode(PstCd);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("Othr")) {
                    String Othr = xmlStreamReader.getElementText();
                    logger.debug(Othr);
                    // TODO: Handler Othr here.
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


    private static String getClrSysMmbId(XMLStreamReader xmlStreamReader, String partyIdentifier) throws XMLStreamException {
        String ClrSysMmbId = null;
        while (xmlStreamReader.hasNext()) {
            xmlStreamReader.next();
//            printEvent(xmlStreamReader);
            if (xmlStreamReader.getEventType() == XMLStreamReader.START_ELEMENT)
            {
            if (xmlStreamReader.getLocalName().equalsIgnoreCase("MmbId")) {
                String MmbId = xmlStreamReader.getElementText();
                ClrSysMmbId = MmbId;
                    logger.error(ClrSysMmbId);
                }
            }

            if (xmlStreamReader.getEventType() == XMLStreamReader.END_ELEMENT) {
                if (xmlStreamReader.getLocalName().equalsIgnoreCase(partyIdentifier)) {
                    break;
                }
            }
        }
        return ClrSysMmbId;
    }



//    <xs:complexType name="FinancialInstitutionIdentification18">
//        <xs:sequence>
//            <xs:element maxOccurs="1" minOccurs="0" name="BICFI" type="BICFIDec2014Identifier"/>
//            <xs:element maxOccurs="1" minOccurs="0" name="ClrSysMmbId" type="ClearingSystemMemberIdentification2"/>
//            <xs:element maxOccurs="1" minOccurs="0" name="LEI" type="LEIIdentifier"/>
//            <xs:element maxOccurs="1" minOccurs="0" name="Nm" type="Max140Text"/>
//            <xs:element maxOccurs="1" minOccurs="0" name="PstlAdr" type="PostalAddress24"/>
//            <xs:element maxOccurs="1" minOccurs="0" name="Othr" type="GenericFinancialIdentification1"/>
//        </xs:sequence>
//    </xs:complexType>



}
