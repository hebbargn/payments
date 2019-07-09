package com.mejesticpay.paymentfactory;

import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentbase.Genesis;
import com.mejesticpay.paymentbase.AuditEntry;
import com.mejesticpay.service.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class PaymentImpl extends TransactionImpl implements Payment
{
    /*
     * Mandatory - Original Data used for payment creation and can't be changed;
     */
    @Getter
    private Genesis genesis;

    /*Properties set during payment genesis at Gateway*/
    private String source;
    private String subSource;


    /* Properties added by the system during payment Creation and not changeable.
    /* Uniquely Identifies within our platform */
    private String PaymentIdentifier;
    private String branch;

    /* Validate and Enriched by CreditEnrichment Service */
    private CreditEnrichment creditEnrichment;

    /* Validate and Enriched by DebitEnrichment Service */
    private DebitEnrichment debitEnrichment;

    /* Validate and Enriched by Route Determination Service */
    private RoutePayment routeData;

    private List<AuditEntry>auditEntries = new ArrayList<>();


    /* */
    private FundControlServiceInfo fundServiceInfo;

    private SanctionsCheckInfo sanctionsCheckInfo;

    private AccountingInfo accountingInfo;

    private FraudCheckInfo fraudCheckInfo;

    public PaymentImpl(Genesis genesis)
    {
        this.genesis = genesis;
    }

    public void addAuditEntry(AuditEntry auditEntry)
    {
        auditEntries.add(auditEntry);
    }
    public void addAuditEntries(List<AuditEntry> auditEntries)
    {
        this.auditEntries.addAll(auditEntries);
    }

}
