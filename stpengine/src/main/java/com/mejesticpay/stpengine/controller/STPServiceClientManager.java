package com.mejesticpay.stpengine.controller;

import io.rsocket.Payload;
import org.reactivestreams.Subscriber;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class STPServiceClientManager
{
    Map<String, Subscriber<? super Payload>> serviceMap = new HashMap<>();

    public void addService(String serviceName, Subscriber<? super Payload> subscriber)
    {
        serviceMap.put(serviceName,subscriber);
    }

    public Subscriber<? super Payload> getSubscriber(String service)
    {
        return serviceMap.get(service);
    }

    public void removeService(String service)
    {
        serviceMap.remove(service);
    }

    public Set<String> getServiceNames()
    {
        return serviceMap.keySet();
    }
}
