package com.mejesticpay.stpengine.controller;

import com.mejesticpay.stpengine.processors.RequestProcessor;
import com.mejesticpay.stpengine.processors.RoutePaymentProcessor;
import com.mejesticpay.stpengine.processors.STPServiceMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class STPServiceRequestProcessor
{
    Map<String,RequestProcessor> processors = new HashMap<>();
    @Value("${CommandStoreURL}")
    private String commandStoreURL;

    @Autowired
    private STPServiceClientManager stpServiceClientManager;


    @PostConstruct
    public void init()
    {
        processors.put("InboundCTDebitEnrichment",new STPServiceMessageProcessor(stpServiceClientManager,commandStoreURL));
        processors.put("InboundCTCreditEnrichment",new STPServiceMessageProcessor(stpServiceClientManager,commandStoreURL));
        processors.put("RoutePayment",new STPServiceMessageProcessor(stpServiceClientManager,commandStoreURL));
    }

    public  RequestProcessor getRequestProcessor(String name)
    {
        return processors.get(name);
    }
}
