package com.mejesticpay.commandstore.executor;

import com.mejesticpay.commands.Command;
import com.mejesticpay.commands.STPServiceCommand;
import com.mejesticpay.commandstore.model.*;
import com.mejesticpay.paymentbase.AuditEntry;
import com.mejesticpay.paymentbase.InFlightTransactionInfo;
import com.mejesticpay.paymentbase.ServiceFeed;
import com.mejesticpay.service.CreditEnrichment;
import com.mejesticpay.service.DebitEnrichment;
import com.mejesticpay.util.JSONHelper;

import java.time.Instant;

public class QualifyCreditorExecutor extends BaseExecutor
{

    public QualifyCreditorExecutor(CommandStoreContext context)
    {
        super(context);
    }
    public CommandModel execute(Command command)
    {
        try
        {
            STPServiceCommand stpServiceCommand = (STPServiceCommand)command;
            ServiceFeed serviceFeed = stpServiceCommand.getServiceFeed();
            InFlightTransactionInfo inFlight = serviceFeed.getInFlightTransactionInfo();

            int newVersion = inFlight.getVersion() + 1;
            PaymentId paymentID = new PaymentId(inFlight.getPaymentIdentifier(), newVersion);

            PaymentTransactionModel transaction = updateTransaction(paymentID,inFlight );

            CreditEnrichment creditEnrichment = (CreditEnrichment)serviceFeed.getServiceData();
            CreditEnrichmentModel creditEnrichmentModel = new CreditEnrichmentModel();
            creditEnrichmentModel.setPaymentId(paymentID);
            creditEnrichmentModel.setCreditAccount(creditEnrichment.getCreditParty().getAccountNumber());
            creditEnrichmentModel.setCreditValueDate(creditEnrichment.getSettlementDate());
            creditEnrichmentModel.setJson_credit_enrich(JSONHelper.convertToStringFromObject(creditEnrichment));
            creditEnrichmentModel.setCreatedTime(Instant.now());
            context.getCreditEnrichmentRepos().save(creditEnrichmentModel);

            saveAuditEntries("CreditEnrichment", inFlight.getPaymentIdentifier(),serviceFeed.getAuditEntries());

            return transaction;

        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(),e);
        }

    }

}