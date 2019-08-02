package com.mejesticpay.paymentbase;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BranchAndFinancialInstitution
{
    private String BICFI;

    private String clrSysMmbId;
    private String clrSysIdCode;
    private String clrSysIdPrtry;

    private String name;

    private PostalAddress address;

    private String otherId;
    private String schmeNmCode;
    private String schmeNmPrtry;
    private String issr;

    private String branchId;
    private PostalAddress branchAddress;
}
