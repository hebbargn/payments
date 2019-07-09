package com.mejesticpay.service;

import com.mejesticpay.paymentbase.Party;
import com.mejesticpay.paymentbase.ServiceData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter @Getter @NoArgsConstructor @ToString
public class DebitEnrichment  implements ServiceData
{
    /* DEBIT SIDE Enrichment */
    private Party debitParty;

    private List<Party> debitPayChain;

}
