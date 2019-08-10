package com.mejesticpay.enrichment.util;

import com.mejesticpay.paymentbase.Party;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyLookupRequest
{
    public static enum LookupType
    {
        CIF,
        GL,
        CLEARING
    }

    public PartyLookupRequest(LookupType type)
    {
        this.type = type;
    }
    private LookupType type;
    private String cifRequestURL;

    private Party party;

    private String clearing;

    private String glAccount;

    private String cifAccount;

}
