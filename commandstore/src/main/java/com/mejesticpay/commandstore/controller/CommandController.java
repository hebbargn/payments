package com.mejesticpay.commandstore.controller;

import com.mejesticpay.commands.CreatePaymentCommand;
import com.mejesticpay.commands.STPServiceCommand;
import com.mejesticpay.commandstore.executor.*;
import com.mejesticpay.commandstore.model.PaymentTransactionModel;
import com.mejesticpay.commandstore.model.repos.*;

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
    public PaymentTransactionModel createPayment(@RequestBody CreatePaymentCommand createPaymentCommand)
    {
        return (PaymentTransactionModel)new CreatePaymentExecutor(commandStoreContext).execute(createPaymentCommand);
    }

    @Transactional
    @PostMapping("/stpupdate")
    public PaymentTransactionModel updateSTPCommands(@RequestBody STPServiceCommand stpServiceCommand)
    {
        switch(stpServiceCommand.getServiceType())
        {
            case FraudCheckInfo:
                return (PaymentTransactionModel)new FraudCheckExecutor(commandStoreContext).execute(stpServiceCommand);
            case DebitEnrichment:
                return (PaymentTransactionModel)new QualifyDebtorExecutor(commandStoreContext).execute(stpServiceCommand);
            case CompletePayment:
                return (PaymentTransactionModel) new CompletePaymentExecutor(commandStoreContext).execute(stpServiceCommand);
            case CreditEnrichment:
                return (PaymentTransactionModel) new QualifyCreditorExecutor(commandStoreContext).execute(stpServiceCommand);

            default:
                return null;
        }

    }
}
