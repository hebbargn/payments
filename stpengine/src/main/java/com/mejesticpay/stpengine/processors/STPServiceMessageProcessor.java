package com.mejesticpay.stpengine.processors;

import com.mejesticpay.stpengine.controller.STPServiceClientManager;

public class STPServiceMessageProcessor extends AbstractRouteProcessor
{
    public STPServiceMessageProcessor(STPServiceClientManager stpServiceClientManager,String commandStoreURL)
    {
        super(stpServiceClientManager,commandStoreURL);
    }
    @Override
    public void process(String data)
    {
        System.out.println("Message is here" + data);
        super.process(data);
        System.out.println("Message is successfully processed");
    }
}
