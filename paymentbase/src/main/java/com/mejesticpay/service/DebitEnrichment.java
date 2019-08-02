package com.mejesticpay.service;

import com.mejesticpay.paymentbase.Party;
import com.mejesticpay.paymentbase.ServiceData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @NoArgsConstructor @ToString
public class DebitEnrichment  implements ServiceData
{
    /* DEBIT SIDE Enrichment */
    private Party debitParty;

    /**
     * Debit customer
     */
    private Party debtor;
    /**
     * Customer's bank. Customer has account with this bank.
     */
    private Party debtorAgent;
    /**
     * Indirect participant that has account with this bank.
     */
    private Party indirectParticipant;

}
