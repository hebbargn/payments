package com.mejesticpay.iso20022.type;

import com.mejesticpay.iso20022.base.XMLParser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Setter
@Getter
@NoArgsConstructor
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

}
