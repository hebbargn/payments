package com.mejesticpay.store.mongo;

import com.mejesticpay.paymentbase.Payment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter @Setter
@NoArgsConstructor
public class PaymentWrapper
{
    @Id
    private String paymentRef;
    private Payment payment;
    public PaymentWrapper(String ref, Payment payment)
    {
        paymentRef = ref;
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }
}
