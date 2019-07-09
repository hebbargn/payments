package com.mejesticpay.paymentfactory;

import com.mejesticpay.paymentbase.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public abstract class TransactionImpl implements Transaction
{
    private String state;
    private String status;
    private String station;
    private String track;

    /*Properties are automatically added by the system*/
    private Instant createdTime;
    private Instant lastUpdatedTime;
    private String  createdBy;
    private String  lastUpdatedBy;

    private int	version;

    public void incrementVersion() { ++ version;}

}
