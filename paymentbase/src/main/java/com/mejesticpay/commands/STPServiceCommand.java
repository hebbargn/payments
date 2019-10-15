package com.mejesticpay.commands;

import com.mejesticpay.paymentbase.AuditEntry;
import com.mejesticpay.paymentbase.InFlightTransactionInfo;
import com.mejesticpay.paymentbase.ServiceData;
import com.mejesticpay.paymentbase.ServiceFeed;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class STPServiceCommand  implements Command
{
    public enum ServiceType
    {
        CreditEnrichment,
        DebitEnrichment,
        FraudCheckInfo,
        CompletePayment
    }
    private ServiceType serviceType;
    private ServiceData serviceData;
    private InFlightTransactionInfo transactionInfo;
    private List<AuditEntry> auditEntries = new ArrayList<>();
    //private ServiceFeed serviceFeed;

    public STPServiceCommand(ServiceType serviceType, ServiceData serviceData, InFlightTransactionInfo transactionInfo)
    {
        this.serviceData = serviceData;
        this.serviceType = serviceType;
        this.transactionInfo = transactionInfo;
    }

}
