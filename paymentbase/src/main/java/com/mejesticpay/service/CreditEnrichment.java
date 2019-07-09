package com.mejesticpay.service;

import com.mejesticpay.paymentbase.Party;
import com.mejesticpay.paymentbase.ServiceData;

import java.time.LocalDate;
import java.util.List;

public class CreditEnrichment  implements ServiceData
{
    /*
    * CREDIT SIDE ENRICHMENT
    * */
    private Party creditParty;

    private List<Party>creditPayChain;

    private String routeType;
    private LocalDate settlementDate;
    private LocalDate clearingDate;

}
