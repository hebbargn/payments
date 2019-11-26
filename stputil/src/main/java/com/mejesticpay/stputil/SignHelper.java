package com.mejesticpay.stputil;

import sun.misc.BASE64Encoder;

import java.security.*;

public class SignHelper
{
    public static String sign(String data, PrivateKey privateKey) throws InvalidKeyException,NoSuchAlgorithmException,SignatureException
    {
        Signature rsa = Signature.getInstance("SHA256withRSA");
        rsa.initSign(privateKey);
        rsa.update(data.getBytes());
        byte[]signatureBytes = rsa.sign();
        return new BASE64Encoder().encode(signatureBytes);
    }
}
