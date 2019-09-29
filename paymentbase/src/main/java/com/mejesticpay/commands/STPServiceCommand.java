package com.mejesticpay.commands;

import com.mejesticpay.paymentbase.ServiceFeed;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private ServiceFeed serviceFeed;

    public STPServiceCommand(ServiceType serviceType, ServiceFeed serviceFeed)
    {
        this.serviceFeed = serviceFeed;
        this.serviceType = serviceType;
    }

}
