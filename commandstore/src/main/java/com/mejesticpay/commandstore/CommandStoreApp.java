package com.mejesticpay.commandstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Signature;

@SpringBootApplication
public class CommandStoreApp
{
    public static void main(String []args)
    {
        SpringApplication.run(CommandStoreApp.class,args);
    }

}
