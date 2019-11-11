package com.mejesticpay.eventstore.executor;

import com.mejesticpay.events.Event;
import com.mejesticpay.events.UpdatePaymentEvent;
import com.mejesticpay.eventstore.model.PaymentWrapper;

import java.util.Optional;

public class UpdatePaymentEventExecutor implements EventExecutor
{
    EventStoreContext eventStoreContext;

    public UpdatePaymentEventExecutor(EventStoreContext context) {
        eventStoreContext = context;
    }

    @Override
    public void handleEvent(Event updatePaymentEvent)
    {
        UpdatePaymentEvent event = (UpdatePaymentEvent) updatePaymentEvent;
        if(event == null)
        {
            // TBD: should we throw exception?
            System.out.println(this.getClass().toString() +  " null event received. escaping update ... ");
        }

        System.out.println(this.getClass().toString() +  " : handling update event for payment: " + event.getTransactionInfo().getPaymentIdentifier());

        Optional<PaymentWrapper> optional = eventStoreContext.getPaymentRepository().findById(event.getTransactionInfo().getPaymentIdentifier());
        if(optional.isPresent())
        {
            // Get Payment object
            PaymentWrapper pw = optional.get();
            // Update in-flight transaction info like Stage, State etc.
            pw.updatePayment(event.getTransactionInfo());
            // Update service specific info on payment object.
            pw.updatePayment(event.getServiceType(), event.getServiceData());
            // Save payment.
            eventStoreContext.getPaymentRepository().save(pw);
        }
    }
}
