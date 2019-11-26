package com.mejesticpay.stputil;

import com.mejesticpay.paymentbase.Payment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class STPProcessingResult
{
    public STPProcessingResult(boolean isSuccess)
    {
        this.success = isSuccess;
    }
    private boolean success;
    private String nextStation;
    private Payment payment;
}
