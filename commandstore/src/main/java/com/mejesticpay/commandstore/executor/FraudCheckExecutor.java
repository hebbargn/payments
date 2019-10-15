package com.mejesticpay.commandstore.executor;

import com.mejesticpay.commands.Command;
import com.mejesticpay.commands.STPServiceCommand;
import com.mejesticpay.commandstore.model.*;
import com.mejesticpay.paymentbase.InFlightTransactionInfo;
import com.mejesticpay.paymentbase.ServiceFeed;
import com.mejesticpay.service.CreditEnrichment;
import com.mejesticpay.service.FraudCheckInfo;
import com.mejesticpay.util.JSONHelper;

import java.time.Instant;

public class FraudCheckExecutor  extends BaseExecutor {

    public FraudCheckExecutor(CommandStoreContext context) {
        super(context);
    }

    public CommandModel execute(Command command) {
        try
        {
            STPServiceCommand stpServiceCommand = (STPServiceCommand) command;
            InFlightTransactionInfo inFlight = stpServiceCommand.getTransactionInfo();

            int newVersion = inFlight.getVersion() + 1;
            PaymentId paymentID = new PaymentId(inFlight.getPaymentIdentifier(), newVersion);

            PaymentTransactionModel transaction = updateTransaction(paymentID, inFlight);

            FraudCheckInfo fraudCheckInfo = (FraudCheckInfo)stpServiceCommand.getServiceData();

            FraudCheckModel fraudCheckModel = new FraudCheckModel();
            fraudCheckModel.setPaymentId(paymentID);
            fraudCheckModel.setFraudCheckDate(fraudCheckInfo.getFraudCheckDate());
            fraudCheckModel.setFraudCheckStatus(fraudCheckInfo.getFraudCheckStatus());
            fraudCheckModel.setJson_fraud_check(fraudCheckInfo.getFraudCheckData());
            fraudCheckModel.setCreatedTime(Instant.now());
            context.getFraudCheckRepos().save(fraudCheckModel);

            saveAuditEntries("FraudCheck", inFlight.getPaymentIdentifier(), stpServiceCommand.getAuditEntries());

            return transaction;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

    }
}
