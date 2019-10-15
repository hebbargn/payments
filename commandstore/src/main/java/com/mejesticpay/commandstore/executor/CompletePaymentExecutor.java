package com.mejesticpay.commandstore.executor;

import com.mejesticpay.commands.Command;
import com.mejesticpay.commands.STPServiceCommand;
import com.mejesticpay.commandstore.model.CommandModel;
import com.mejesticpay.commandstore.model.FraudCheckModel;
import com.mejesticpay.commandstore.model.PaymentId;
import com.mejesticpay.commandstore.model.PaymentTransactionModel;
import com.mejesticpay.paymentbase.InFlightTransactionInfo;
import com.mejesticpay.paymentbase.ServiceFeed;
import com.mejesticpay.service.FraudCheckInfo;

import java.time.Instant;

public class CompletePaymentExecutor  extends BaseExecutor
{

    public CompletePaymentExecutor(CommandStoreContext context)
    {
        super(context);
    }

    public CommandModel execute( Command command)
    {
        try
        {
            STPServiceCommand stpServiceCommand = (STPServiceCommand) command;
            InFlightTransactionInfo inFlight = stpServiceCommand.getTransactionInfo();

            int newVersion = inFlight.getVersion() + 1;
            PaymentId paymentID = new PaymentId(inFlight.getPaymentIdentifier(), newVersion);
            PaymentTransactionModel transaction = updateTransaction(paymentID, inFlight);

            saveAuditEntries("CompletePayment", inFlight.getPaymentIdentifier(), stpServiceCommand.getAuditEntries());

            return transaction;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
