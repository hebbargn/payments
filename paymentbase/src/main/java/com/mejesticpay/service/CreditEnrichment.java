package com.mejesticpay.service;

import com.mejesticpay.paymentbase.Party;
import com.mejesticpay.paymentbase.ServiceData;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * CREDIT SIDE ENRICHMENT return by credit enrichment service.
 */
@Getter
@Setter
public class CreditEnrichment  implements ServiceData
{
    /*
    * This is actual party with credit account.
    * For outgoing clearing payment, it will be clearing account.
    * For book payment, it will be customer account.
    * */
    private Party creditParty;
    /**
     * credit customer
     */
    private Party creditor;
    /**
     * Customer's bank. Customer has account with this bank.
     */
    private Party creditorAgent;
    /**
     * Indirect participant that has account with this bank.
     */
    private Party indirectParticipant;

    private String routeType;
    private LocalDate settlementDate;
    private LocalDate clearingDate;

}
