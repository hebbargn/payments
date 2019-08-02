package com.mejesticpay.paymentbase;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PostalAddress
{
    private String  adrTp;
    private String  dept;
    private String  subDept;
    private String  strtNm;
    private String  bldgNb;
    private String  bldgNm;
    private String  flr;
    private String  pstBx;
    private String  room;
    private String  pstCd;
    private String  twnNm;
    private String  twnLctnNm;
    private String  dstrctNm;
    private String  ctrySubDvsn;
    private String  ctry;
    private String  adrLine;
}
