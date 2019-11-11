package com.mejesticpay.eventstore.controller;

import com.mejesticpay.events.CreatePaymentEvent;
import com.mejesticpay.events.UpdatePaymentEvent;
import com.mejesticpay.eventstore.executor.CreatePaymentEventExecutor;
import com.mejesticpay.eventstore.executor.EventStoreContext;
import com.mejesticpay.eventstore.executor.UpdatePaymentEventExecutor;
import com.mejesticpay.eventstore.model.repos.PaymentRepository;
import com.mejesticpay.util.JSONHelper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class EventController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${EventStore:CreatePaymentEvent}")
    public final String serviceName = "CreatePaymentEvent";

    @Autowired
    private PaymentRepository repository;

    private EventStoreContext eventStoreContext;

    @PostConstruct
    public void init()
    {
        eventStoreContext = new EventStoreContext();
        eventStoreContext.setPaymentRepository(repository);
    }


    @KafkaListener(topics = "CreatePaymentEvent")
    public void receiveCreateMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment)
    {
        try {
            //logger.info(String.format("Topic - %s, Partition - %d, Value = %s", serviceName, record.partition(), record.value()));

            System.out.println("Receive create payment event.");
            System.out.println(record.value());
            CreatePaymentEvent event = JSONHelper.convertToObjectFromJson(record.value(), CreatePaymentEvent.class);
            new CreatePaymentEventExecutor(eventStoreContext).handleEvent(event);

        } catch (IOException e) {
            e.printStackTrace();
        }
        acknowledgment.acknowledge();
    }


    @KafkaListener(topics = "UpdatePaymentEvent")
    public void receiveUpdateMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment)
    {
        try {
            //logger.info(String.format("Topic - %s, Partition - %d, Value = %s", serviceName, record.partition(), record.value()));

            System.out.println("Receive Update Payment event.");
            System.out.println(record.value());
            UpdatePaymentEvent event = JSONHelper.convertToObjectFromJson(record.value(), UpdatePaymentEvent.class);
            new UpdatePaymentEventExecutor(eventStoreContext).handleEvent(event);

        } catch (IOException e) {
            e.printStackTrace();
        }
        acknowledgment.acknowledge();
    }
}
