package com.mejesticpay.stpengine;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class StartSTPEngine
{
    public static void main(String []args)
    {
        new SpringApplicationBuilder()
                .main(StartSTPEngine.class)
                .sources(StartSTPEngine.class)
                //.profiles("server")
                .run(args);
    }


}
