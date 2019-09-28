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
@Table(name="complete_payment")
public class CompletePaymentModel extends CommonBase  implements Persistable<PaymentId>
{
    @EmbeddedId
    private PaymentId paymentId;

    private LocalDate completeDate;


    private String json_complete_payment;


    public boolean isNew()
    {
        return true;
    }

    public PaymentId getId()
    {
        return paymentId;
    }
}
