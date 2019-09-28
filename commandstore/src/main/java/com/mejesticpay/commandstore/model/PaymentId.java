package com.mejesticpay.commandstore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter

@Embeddable
public class PaymentId implements Serializable
{
    private String paymentRef;
    private Integer version;

    public PaymentId() {
    }

    public PaymentId(String paymentRef,Integer version) {
        this.paymentRef = paymentRef;
        this.version = version;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentId)) return false;
        PaymentId that = (PaymentId) o;
        return Objects.equals(getPaymentRef(), that.getPaymentRef()) &&
                Objects.equals(getVersion(), that.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPaymentRef(), getVersion());
    }
}
