package com.mejesticpay.enrichment.util;

import com.mejesticpay.paymentbase.Party;

public interface PartyLookUp
{
    public PartyLookupResponse lookup(PartyLookupRequest request);
}
