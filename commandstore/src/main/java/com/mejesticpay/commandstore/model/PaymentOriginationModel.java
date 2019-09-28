package com.mejesticpay.commandstore.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name="payment_origination")
public class PaymentOriginationModel extends CommonBase  implements Persistable<String>
{
    @Id
    private String paymentReference;
    private String json_genesis;
    private String source;

    public boolean isNew()
    {
        return true;
    }

    public String getId()
    {
        return paymentReference;
    }

}
