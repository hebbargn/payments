package com.mejesticpay.iso20022.type;


import com.mejesticpay.iso20022.base.XMLParser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class CashAccount38 extends XMLParser
{
    private static final Logger logger = LogManager.getLogger(CashAccount38.class);

    public static String getAccount(XMLStreamReader xmlStreamReader, String partyIdentifier) throws XMLStreamException
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

//    <xs:element name="Id" type="AccountIdentification4Choice"/>
//            <xs:element maxOccurs="1" minOccurs="0" name="Tp" type="CashAccountType2Choice"/>
//            <xs:element maxOccurs="1" minOccurs="0" name="Ccy" type="ActiveOrHistoricCurrencyCode"/>
//            <xs:element maxOccurs="1" minOccurs="0" name="Nm" type="Max70Text"/>
//            <xs:element maxOccurs="1" minOccurs="0" name="Prxy" type="ProxyAccountIdentification1"/> <xs:element name="Id" type="AccountIdentification4Choice"/>

}
