package com.mejesticpay.stpengine;

import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.stpengine.controller.STPServiceClientManager;
import com.mejesticpay.stpengine.controller.STPServiceRequestProcessor;
import io.rsocket.Payload;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public class STPClientRequestHandler implements Publisher<Payload>
{
    private String serviceName;
    private STPServiceClientManager stpServiceClientManager;
    private STPServiceRequestProcessor stpServiceRequestProcessor;

    public STPClientRequestHandler(STPServiceClientManager controller, STPServiceRequestProcessor stpServiceRequestProcessor)
    {
        this.stpServiceClientManager = controller;
        this.stpServiceRequestProcessor = stpServiceRequestProcessor;
    }
    @Override
    public void subscribe(Subscriber<? super Payload> subscriber)
    {
        stpServiceClientManager.addService(serviceName, subscriber);
        System.out.println("Subscribed for service "+ serviceName);
    }

    public void processPayload(Payload payload)
    {
        String data = payload.getDataUtf8();
        String service = payload.getMetadataUtf8();
        if(data.startsWith("!!CLIENT_CHANNEL_REGISTER!!"))
        {
            this.serviceName = service;
            System.out.println("Channel successfully registered for " + serviceName);
        }
        else
        {
              stpServiceRequestProcessor.getRequestProcessor(service).process(data);
        }
        payload.release();
    }

    public void unSubscribe()
    {
        stpServiceClientManager.removeService(serviceName);
        System.out.println("Unregistered Service " + serviceName);
    }


}
