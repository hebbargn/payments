package com.mejesticpay.commandstore.controller;

import com.mejesticpay.commands.CreatePaymentCommand;
import com.mejesticpay.commands.STPServiceCommand;
import com.mejesticpay.commandstore.executor.*;
import com.mejesticpay.commandstore.model.PaymentTransactionModel;
import com.mejesticpay.commandstore.model.repos.*;

import com.mejesticpay.paymentbase.InFlightTransactionInfo;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentfactory.PaymentImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RequestMapping("commands")
@RestController
public class CommandController
{

    @Autowired
    private AuditEntryRepos auditEntryRepos;
    @Autowired
    private CreditEnrichmentRepos creditEnrichmentRepos;
    @Autowired
    private DebitEnrichmentRepos debitEnrichmentRepos;
    @Autowired
    private PaymentOriginationRepos paymentOriginationRepos;
    @Autowired
    private PaymentTransactionRepos paymentTransactionRepos;
    @Autowired
    private FraudCheckRepos fraudCheckRepos;

    private CommandStoreContext commandStoreContext;
    @PostConstruct
    public void init()
    {
        commandStoreContext = new CommandStoreContext();
        commandStoreContext.setAuditEntryRepos(auditEntryRepos);
        commandStoreContext.setCreditEnrichmentRepos(creditEnrichmentRepos);
        commandStoreContext.setDebitEnrichmentRepos(debitEnrichmentRepos);
        commandStoreContext.setPaymentOriginationRepos(paymentOriginationRepos);
        commandStoreContext.setPaymentTransactionRepos(paymentTransactionRepos);
        commandStoreContext.setFraudCheckRepos(fraudCheckRepos);
    }

    @Transactional
    @PostMapping("/createpayment")
    public Payment createPayment(@RequestBody CreatePaymentCommand createPaymentCommand)
    {
        PaymentTransactionModel transactionModel = (PaymentTransactionModel)new CreatePaymentExecutor(commandStoreContext).execute(createPaymentCommand);

        PaymentImpl payment = new PaymentImpl(createPaymentCommand.getGenesis());
        payment.setPaymentIdentifier(transactionModel.getPaymentId().getPaymentRef());
        payment.setVersion(transactionModel.getPaymentId().getVersion());
        payment.setState(transactionModel.getState());
        payment.setSource(createPaymentCommand.getSource());
        payment.setBranch(transactionModel.getBranch());
        payment.setTrack(transactionModel.getTrack());
        payment.setStation(transactionModel.getStation());
        payment.setStatus(transactionModel.getStatus());

        return payment;
    }

    @Transactional
    @PostMapping("/stpupdate")
    public InFlightTransactionInfo updateSTPCommands(@RequestBody STPServiceCommand stpServiceCommand)
    {
        PaymentTransactionModel transactionModel;
        switch(stpServiceCommand.getServiceType())
        {
            case FraudCheckInfo:
                transactionModel =  (PaymentTransactionModel)new FraudCheckExecutor(commandStoreContext).execute(stpServiceCommand);
                break;
            case DebitEnrichment:
                transactionModel =  (PaymentTransactionModel)new QualifyDebtorExecutor(commandStoreContext).execute(stpServiceCommand);
                break;
            case CompletePayment:
                transactionModel =  (PaymentTransactionModel) new CompletePaymentExecutor(commandStoreContext).execute(stpServiceCommand);
                break;
            case CreditEnrichment:
                transactionModel =  (PaymentTransactionModel) new QualifyCreditorExecutor(commandStoreContext).execute(stpServiceCommand);
                break;

            default:
                return null;
        }
        InFlightTransactionInfo inFlightTransactionInfo = new InFlightTransactionInfo();
        inFlightTransactionInfo.setBranch(transactionModel.getBranch());
        inFlightTransactionInfo.setCurrentStation(transactionModel.getStation());
        inFlightTransactionInfo.setPaymentIdentifier(transactionModel.getPaymentId().getPaymentRef());
        inFlightTransactionInfo.setState(transactionModel.getState());
        inFlightTransactionInfo.setStatus(transactionModel.getStatus());
        inFlightTransactionInfo.setTrack(transactionModel.getTrack());
        inFlightTransactionInfo.setVersion(transactionModel.getPaymentId().getVersion());

        return inFlightTransactionInfo;

    }
}
