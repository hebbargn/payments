package com.mejesticpay.stpengine.processors;

import com.mejesticpay.stpengine.controller.STPServiceClientManager;

public class RoutePaymentProcessor extends AbstractRouteProcessor
{
    public RoutePaymentProcessor(STPServiceClientManager stpServiceClientManager,String commandStoreURL)
    {
        super(stpServiceClientManager,commandStoreURL);
    }

    @Override
    public void process(String data)
    {
        System.out.println("Message is here" + data);

    }
}
