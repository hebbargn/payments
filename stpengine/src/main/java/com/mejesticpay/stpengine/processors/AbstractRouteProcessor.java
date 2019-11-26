package com.mejesticpay.stpengine.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.stp.STPInboundData;
import com.mejesticpay.stpengine.controller.STPServiceClientManager;
import com.mejesticpay.stputil.STPMessageProcessor;
import com.mejesticpay.stputil.STPProcessingResult;
import com.mejesticpay.util.JSONHelper;
import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractRouteProcessor implements RequestProcessor
{
    RestTemplate restTemplate;
    String commandStoreURL;
    STPServiceClientManager stpServiceClientManager;
    public AbstractRouteProcessor(STPServiceClientManager stpServiceClientManager,String commandStoreURL)
    {
        this.commandStoreURL = commandStoreURL;
        this.restTemplate = new RestTemplate();
        this.stpServiceClientManager = stpServiceClientManager;
    }
    @Override
    public void process(String data)
    {
        System.out.println("Message is here" + data);
        STPInboundData inboundData = null;
        try
        {
            inboundData = JSONHelper.convertToObjectFromJson(data, STPInboundData.class);
            STPMessageProcessor stpMessageProcessor = new STPMessageProcessor(restTemplate,commandStoreURL);
            STPProcessingResult result = stpMessageProcessor.processInboundData(inboundData);
            if(result.isSuccess())
            {
                routePaymentToNextStation(result.getPayment(),result.getNextStation());
            }
            else
            {
                System.out.println("We have a problem " + data);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    void routePaymentToNextStation(Payment payment, String station) throws JsonProcessingException
    {
        Payload payload = DefaultPayload.create(JSONHelper.convertToStringFromObject(payment),"STPServiceMessage");
        if(stpServiceClientManager.getSubscriber(station) != null) {
            stpServiceClientManager.getSubscriber(station).onNext(payload);
            System.out.println("Successfully routed to Next Station " + station);
        }
        else throw new RuntimeException(station +" Client is not subscribed");
    }
}
