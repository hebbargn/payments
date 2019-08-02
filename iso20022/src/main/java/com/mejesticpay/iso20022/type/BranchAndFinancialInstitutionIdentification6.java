package com.mejesticpay.iso20022.type;

import com.mejesticpay.iso20022.base.XMLParser;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
