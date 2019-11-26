package com.mejesticpay.services;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class StartEnrichServices
{
    public static void  main(String []args)
    {
        new SpringApplicationBuilder()
                .main(StartEnrichServices.class)
                .sources(StartEnrichServices.class)
                .profiles("enrich")
                .run(args);
    }
}
