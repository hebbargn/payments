package com.mejesticpay.enrichment.util;

import com.mejesticpay.cifservice.model.Account;
import com.mejesticpay.cifservice.model.Customer;
import com.mejesticpay.paymentbase.Party;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class CIFLookup implements PartyLookUp
{

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public PartyLookupResponse lookup(PartyLookupRequest request)
    {
        PartyLookupResponse result = new PartyLookupResponse();

        String finalURL = request.getCifRequestURL() + request.getCifAccount();

        System.out.println("finalURL " + finalURL);
        Account account = restTemplate.getForObject(finalURL,Account.class);

        if(account != null)
        {
            result.setResult(PartyLookupResponse.LookupResult.SUCCESS);
            result.setAccountNumber(account.getAccountNumber());
            result.setAccountType(account.getType());
            result.setAccountName(account.getName());
            result.setAccountStatus(account.getStatus());

            if(account.getCustomers() != null)
            {
                for(Customer customer: account.getCustomers())
                {
                    result.setName(customer.getName());
                    result.setCustomerNumber(customer.getCustomerNumber());
                    result.setStreet(customer.getStreet());
                    result.setBuildingNumber(customer.getBuildingNumber());
                    result.setTownName(customer.getTownName());
                    result.setPostalCode(customer.getPostalCode());
                    result.setCountry(customer.getCountry());
                    break;

                }
            }
        }
        else
        {
            result.setResult(PartyLookupResponse.LookupResult.NOTFOUND);
        }

        return result;
    }
}
