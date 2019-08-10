package com.mejesticpay.enrichment.util;

public class ClearingAccountLookup implements  PartyLookUp
{
    public PartyLookupResponse lookup(PartyLookupRequest request)
    {
        PartyLookupResponse result = new PartyLookupResponse();

        result.setResult(PartyLookupResponse.LookupResult.SUCCESS);
        result.setAccountNumber(request.getClearing() + ".CLEARING_ACCOUNT_NUMBER");
        result.setAccountType(request.getClearing() + ".CLEARING_ACCOUNT_TYPE");
        result.setAccountName(request.getClearing() + ".CLEARING_ACCOUNT_NAME");
        result.setAccountStatus(request.getClearing() + ".CLEARING_ACCOUNT_CURRENCY");

        return result;
    }
}
