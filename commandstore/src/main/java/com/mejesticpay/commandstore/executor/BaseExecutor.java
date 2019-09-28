package com.mejesticpay.commandstore.executor;

import com.mejesticpay.commandstore.model.AuditEntryModel;
import com.mejesticpay.commandstore.model.PaymentId;
import com.mejesticpay.commandstore.model.PaymentTransactionModel;
import com.mejesticpay.paymentbase.AuditEntry;
import com.mejesticpay.paymentbase.InFlightTransactionInfo;

import java.time.Instant;
import java.util.List;

public abstract class BaseExecutor implements Executor
{

    CommandStoreContext context;

    public BaseExecutor(CommandStoreContext context)
    {
        this.context = context;
    }

    void saveAuditEntries(String serviceName, String paymentRef,List<AuditEntry> auditEntries)
    {
        if(auditEntries != null)
        {
            for(AuditEntry entry: auditEntries)
            {
                AuditEntryModel auditEntryModel = new AuditEntryModel(serviceName, paymentRef,
                        entry.getMessage(),entry.getJsonData());
                context.getAuditEntryRepos().save(auditEntryModel);
            }
        }
    }


    PaymentTransactionModel updateTransaction(PaymentId paymentID, InFlightTransactionInfo inFlight )
    {
        PaymentTransactionModel transaction = new PaymentTransactionModel();
        transaction.setPaymentId(paymentID);
        transaction.setState(inFlight.getState());
        transaction.setBranch(inFlight.getBranch());
        transaction.setCreatedTime(Instant.now());
        transaction.setTrack(inFlight.getTrack());
        transaction.setStation(inFlight.getCurrentStation());
        transaction.setStatus(inFlight.getStatus());
        context.getPaymentTransactionRepos().save(transaction);
        return transaction;
    }

}
