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
@Table(name="credit_enrich")
public class CreditEnrichmentModel extends CommonBase  implements Persistable<PaymentId>
{
    @EmbeddedId
    private PaymentId paymentId;

    private LocalDate creditValueDate;
    private String  creditAccount;

    private String json_credit_enrich;

    public boolean isNew()
    {
        return true;
    }

    public PaymentId getId()
    {
        return paymentId;
    }
}
