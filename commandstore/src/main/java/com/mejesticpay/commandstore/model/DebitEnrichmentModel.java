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
@Table(name="debit_enrich")
public class DebitEnrichmentModel extends CommonBase implements Persistable<PaymentId>
{
    @EmbeddedId
    private PaymentId paymentId;

    private LocalDate debitSettlementDate;
    private String  debitAccount;

    private String json_debit_enrich;

    public boolean isNew()
    {
        return true;
    }

    public PaymentId getId()
    {
        return paymentId;
    }

}
