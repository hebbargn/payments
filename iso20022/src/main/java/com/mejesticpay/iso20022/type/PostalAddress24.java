package com.mejesticpay.iso20022.type;

import com.mejesticpay.iso20022.base.XMLParser;
import com.mejesticpay.paymentbase.Party;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class PostalAddress24 extends XMLParser {

    private static final Logger logger = LogManager.getLogger(BranchAndFinancialInstitutionIdentification6.class);
    
    private String  AdrTp;
    private String  Dept;
    private String  SubDept;
    private String  StrtNm;
    private String  BldgNb;
    private String  BldgNm;
    private String  Flr;
    private String  PstBx;
    private String  Room;
    private String  PstCd;
    private String  TwnNm;
    private String  TwnLctnNm;
    private String  DstrctNm;
    private String  CtrySubDvsn;
    private String  Ctry;
    private String AdrLine;

    public static Party getParty(XMLStreamReader xmlStreamReader, String partyIdentifier) throws XMLStreamException
    {
        Party party = new Party();

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
                    String StrtNm = xmlStreamReader.getElementText();
                    logger.debug(StrtNm);
                    party.setStreet(StrtNm);
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
}
