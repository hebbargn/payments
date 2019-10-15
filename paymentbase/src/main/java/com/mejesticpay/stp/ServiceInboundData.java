package com.mejesticpay.stp;

import com.mejesticpay.paymentbase.Payment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceInboundData
{
    /**
     * Read only Payment object and any update made in service, STP dispatcher service will reject the transaction.
     */
    private Payment payment;

    public ServiceInboundData(Payment payment)
    {
        this.payment = payment;
    }
}
