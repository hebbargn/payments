package com.mejesticpay.commandstore.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="fraud_Check")
public class FraudCheckModel extends CommonBase implements Persistable<PaymentId>
{
    @EmbeddedId
    private PaymentId paymentId;

    private String fraudCheckStatus;
    private String json_fraud_check;
    private LocalDate fraudCheckDate;

    public boolean isNew()
    {
        return true;
    }

    public PaymentId getId()
    {
        return paymentId;
    }

}
