package com.mejesticpay.stputil.config;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class KeyConfig
{
    private static PrivateKey privateKey;
    static
    {
        try {

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Files.readAllBytes(Paths.get(KeyConfig.class.getClassLoader()
                    .getResource("keys/privateKey")
                    .toURI())));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privateKey =  kf.generatePrivate(spec);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static PrivateKey getPrivateKey()
    {
        return privateKey;
    }
}
