package com.mejesticpay.eventstore.executor;

import com.mejesticpay.eventstore.model.repos.PaymentRepository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventStoreContext
{

    private PaymentRepository paymentRepository;
}
