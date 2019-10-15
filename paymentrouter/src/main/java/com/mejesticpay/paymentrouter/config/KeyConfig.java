package com.mejesticpay.paymentrouter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

@Configuration
public class KeyConfig
{
    @Bean
    public PrivateKey getPrivateKey()
    {
        try {

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                    .getResource("keys/privateKey")
                    .toURI())));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
