package com.mejesticpay.commandstore.executor;

import com.mejesticpay.commandstore.model.repos.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommandStoreContext
{
    private AuditEntryRepos auditEntryRepos;
    private CreditEnrichmentRepos creditEnrichmentRepos;
    private DebitEnrichmentRepos debitEnrichmentRepos;
    private PaymentOriginationRepos paymentOriginationRepos;
    private PaymentTransactionRepos paymentTransactionRepos;
    private FraudCheckRepos fraudCheckRepos;
}
