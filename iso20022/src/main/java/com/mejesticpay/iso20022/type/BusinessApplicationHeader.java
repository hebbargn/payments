package com.mejesticpay.iso20022.type;

import com.mejesticpay.iso20022.base.XMLParser;
import com.mejesticpay.paymentbase.BranchAndFinancialInstitution;
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
public class BusinessApplicationHeader extends XMLParser
{
    private static final Logger logger = LogManager.getLogger(BusinessApplicationHeader.class);

    private String fromMemberId;
    private String fromBranchId;

    private String toMemberId;
    private String toBranchId;

    // Business Message Identifier. Assigned by the sender of the message
    private String BizMsgIdr;
    // Message Identifier that defines the Business Message. For example: pacs.008.001.06
    private String MsgDefIdr;

    // Indicates whether the message is a Copy, a Duplicate, or a copy of a duplicate of a previously sent ISO 20022 Message.
    private String copyDuplicate;

    // Date and time when this Business Message (header) was created.
    private String creationDttm;

    public static String APP_HDR = "AppHdr";

    public static BusinessApplicationHeader getBussAppHdr( XMLStreamReader xmlStreamReader) throws XMLStreamException
    {
        BusinessApplicationHeader header = new BusinessApplicationHeader();

        while (xmlStreamReader.hasNext())
        {
            xmlStreamReader.next();
            //printEvent(xmlStreamReader);
            if(xmlStreamReader.getEventType()  == XMLStreamReader.START_ELEMENT)
            {

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("Fr")) {

                    while (xmlStreamReader.hasNext())
                    {
                        xmlStreamReader.next();

                        if (xmlStreamReader.getEventType() == XMLStreamReader.START_ELEMENT)
                        {
                            if (xmlStreamReader.getLocalName().equalsIgnoreCase("FIId"))
                            {
                                BranchAndFinancialInstitution fromParty = BranchAndFinancialInstitutionIdentification6.getParty(xmlStreamReader, "FIId");
                                System.out.println(fromParty.getClrSysMmbId());
                                System.out.println(fromParty.getBranchId());

                                header.setFromMemberId(fromParty.getClrSysMmbId());
                                header.setFromBranchId(fromParty.getBranchId());
                                break;
                            }
                        }
                    }
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("To"))
                {
                    while (xmlStreamReader.hasNext())
                    {
                        xmlStreamReader.next();

                        if (xmlStreamReader.getEventType() == XMLStreamReader.START_ELEMENT)
                        {
                            if (xmlStreamReader.getLocalName().equalsIgnoreCase("FIId"))
                            {
                                BranchAndFinancialInstitution toParty = BranchAndFinancialInstitutionIdentification6.getParty(xmlStreamReader, "FIId");
                                System.out.println(toParty.getClrSysMmbId());
                                System.out.println(toParty.getBranchId());

                                header.setToMemberId(toParty.getClrSysMmbId());
                                header.setToBranchId(toParty.getBranchId());
                                break;
                            }
                        }
                    }
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("BizMsgIdr")) {
                    String bizMsgIdr = xmlStreamReader.getElementText();
                    System.out.println(bizMsgIdr);
                    header.setBizMsgIdr(bizMsgIdr);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("MsgDefIdr")) {
                    String msgDefIdr = xmlStreamReader.getElementText();
                    System.out.println(msgDefIdr);
                    header.setMsgDefIdr(msgDefIdr);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("CpyDplct")) {
                    String cpyDplct = xmlStreamReader.getElementText();
                    System.out.println(cpyDplct);
                    header.setCopyDuplicate(cpyDplct);
                }

                if (xmlStreamReader.getLocalName().equalsIgnoreCase("CreDt")) {
                    String creDt = xmlStreamReader.getElementText();
                    System.out.println(creDt);
                    header.setCreationDttm(creDt);
                }
            }

            if (xmlStreamReader.getEventType() == XMLStreamReader.END_ELEMENT)
            {
                if(xmlStreamReader.getLocalName().equalsIgnoreCase(APP_HDR)) {
                    break;
                }
            }
        }

        return header;

    }


}
