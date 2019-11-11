package com.mejesticpay.eventstore.executor;

import com.mejesticpay.events.CreatePaymentEvent;
import com.mejesticpay.events.Event;
import com.mejesticpay.eventstore.model.PaymentWrapper;
import com.mejesticpay.paymentbase.Payment;

public class CreatePaymentEventExecutor implements EventExecutor
{
    EventStoreContext eventStoreContext;

    public CreatePaymentEventExecutor(EventStoreContext context) {
        eventStoreContext = context;
    }

    @Override
    public void handleEvent(Event createPaymentEvent)
    {
        CreatePaymentEvent event = (CreatePaymentEvent) createPaymentEvent;
        Payment payment = event.getPayment();

        PaymentWrapper pw = new PaymentWrapper(payment);
        eventStoreContext.getPaymentRepository().save(pw);
    }
}
