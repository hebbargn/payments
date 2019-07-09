package com.mejesticpay.paymentbase;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InFlightTransactionInfo
{
    private String paymentIdentifier;
    private int version;
    private String currentStation;
    private String state;
    private String requestedState;

    public InFlightTransactionInfo(Payment payment, String currentStation)
    {
        this.paymentIdentifier = payment.getPaymentIdentifier();
        this.version = payment.getVersion();
        this.state = payment.getState();
        this.requestedState = payment.getState();
        this.currentStation = currentStation;
    }
}
