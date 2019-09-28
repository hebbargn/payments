package com.mejesticpay.commandstore.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name="payment_events")
public class PaymentTransactionModel extends CommonBase  implements Persistable<PaymentId>
{
    @EmbeddedId
    private PaymentId paymentId;

    //Transaction
    private String state;
    private String status;
    private String station;
    private String track;
    private String branch;

    public boolean isNew()
    {
        return true;
    }

    public PaymentId getId()
    {
        return paymentId;
    }
}
