package com.mejesticpay.commandstore.executor;

import com.mejesticpay.commands.Command;
import com.mejesticpay.commands.STPServiceCommand;
import com.mejesticpay.commandstore.model.*;
import com.mejesticpay.paymentbase.AuditEntry;
import com.mejesticpay.paymentbase.InFlightTransactionInfo;
import com.mejesticpay.paymentbase.ServiceFeed;
import com.mejesticpay.service.DebitEnrichment;
import com.mejesticpay.util.JSONHelper;

import java.time.Instant;

public class QualifyDebtorExecutor extends BaseExecutor
{

    public QualifyDebtorExecutor(CommandStoreContext context)
    {
        super(context);
    }
    public CommandModel execute(Command command)
    {
        try
        {
            STPServiceCommand stpServiceCommand = (STPServiceCommand)command;
            ServiceFeed serviceFeed = stpServiceCommand.getServiceFeed();
            DebitEnrichment debitEnrichment = (DebitEnrichment)serviceFeed.getServiceData();
            InFlightTransactionInfo inFlight = serviceFeed.getInFlightTransactionInfo();

            int newVersion = inFlight.getVersion() + 1;
            PaymentId paymentID = new PaymentId(inFlight.getPaymentIdentifier(), newVersion);

            PaymentTransactionModel transaction = updateTransaction(paymentID,inFlight );

            DebitEnrichmentModel debitEnrichmentModel = new DebitEnrichmentModel();
            debitEnrichmentModel.setPaymentId(paymentID);
            debitEnrichmentModel.setDebitAccount(debitEnrichment.getDebitParty().getAccountNumber());
            debitEnrichmentModel.setDebitSettlementDate(debitEnrichment.getSettlementDate());
            debitEnrichmentModel.setJson_debit_enrich(JSONHelper.convertToStringFromObject(debitEnrichment));
            debitEnrichmentModel.setCreatedTime(Instant.now());
            context.getDebitEnrichmentRepos().save(debitEnrichmentModel);

            saveAuditEntries("DebitEnrichment", inFlight.getPaymentIdentifier(),serviceFeed.getAuditEntries());

            return transaction;

        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(),e);
        }

    }

}