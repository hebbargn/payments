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
    private String status;
    private String track;
    private String branch;

    public InFlightTransactionInfo(Payment payment, String currentStation)
    {
        this.paymentIdentifier = payment.getPaymentIdentifier();
        this.version = payment.getVersion();
        this.state = payment.getState();
        this.requestedState = payment.getState();
        this.currentStation = currentStation;
        this.branch = payment.getBranch();
        this.status = payment.getStatus();
        this.track = payment.getTrack();
    }
}
