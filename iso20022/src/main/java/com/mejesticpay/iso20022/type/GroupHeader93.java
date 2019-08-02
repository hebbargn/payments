package com.mejesticpay.iso20022.type;

import com.mejesticpay.iso20022.base.XMLParser;
import com.mejesticpay.paymentbase.Genesis;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class GroupHeader93 extends XMLParser
{

    private static final Logger logger = LogManager.getLogger(GroupHeader93.class);

    public static String GROUP_HEADER = "GrpHdr";

    public static void setGroupHeader(XMLStreamReader xmlStreamReader, Genesis genesis) throws XMLStreamException
    {
        while (xmlStreamReader.hasNext())
        {
            xmlStreamReader.next();
            //printEvent(xmlStreamReader);
            if(xmlStreamReader.getEventType()  == XMLStreamReader.START_ELEMENT)
            {

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("IntrBkSttlmDt"))
                {
                    // 2019-07-23
                    String dateAsString = xmlStreamReader.getElementText();
                    System.out.println(dateAsString);
                    LocalDate localDate = LocalDate.parse(dateAsString);
                    //Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    genesis.setSettlementDate(localDate);
                }
            }

            if (xmlStreamReader.getEventType() == XMLStreamReader.END_ELEMENT)
            {
                if(xmlStreamReader.getLocalName().equalsIgnoreCase(GROUP_HEADER)) {
                    break;
                }
            }
        }
    }

}
