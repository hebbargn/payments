package com.mejesticpay.enrichment.util;

import com.mejesticpay.paymentbase.Party;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyLookupResponse
{
    public static enum LookupResult
    {
        SUCCESS,
        NOTFOUND,
        MULTIPLE
    }

    private LookupResult result;

    private String accountNumber;
    private String accountCurrency;
    private String accountName;
    private String accountType;
    private String accountStatus;

    private String customerNumber;
    private String name;
    private String street;
    private String buildingNumber;
    private String postalCode;
    private String townName;
    private String country;

    public void applyToParty(Party party)
    {
        party.setAccountName(accountName);
        party.setAccountNumber(accountNumber);
        party.setAccountCurrency(accountCurrency);
        party.setAccountType(accountType);

        party.setCustomerNumber(customerNumber);
        party.setName(name);
        party.setStreet(street);
        party.setTownName(townName);
        party.setPostalCode(postalCode);
        party.setCountry(country);
    }

}
