package com.mejesticpay.util;

import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class GenerateKeys
{

        private KeyPairGenerator keyGen;
        private KeyPair pair;
        private PrivateKey privateKey;
        private PublicKey publicKey;

        public GenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
            this.keyGen = KeyPairGenerator.getInstance("RSA");
            this.keyGen.initialize(keylength);
        }

        public void createKeys() {
            this.pair = this.keyGen.generateKeyPair();
            this.privateKey = pair.getPrivate();
            this.publicKey = pair.getPublic();
        }

        public PrivateKey getPrivateKey() {
            return this.privateKey;
        }

        public PublicKey getPublicKey() {
            return this.publicKey;
        }

        public void writeToFile(String path, byte[] key) throws IOException {
            File f = new File(path);
            f.getParentFile().mkdirs();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(key);
            fos.flush();
            fos.close();
        }

        public static void main(String[] args) throws Exception {
            GenerateKeys gk;
            try
            {
                gk = new GenerateKeys(1024);
                gk.createKeys();
                gk.writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
                gk.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());


                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(gk.getPrivateKey().getEncoded());
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PrivateKey privateKey =  kf.generatePrivate(spec);

                Signature rsa = Signature.getInstance("SHA256withRSA");
                rsa.initSign(privateKey);
                rsa.update("hebbar fdsfsssssssssssd fkdjshdfkdhdfjkdsd fdjhdfkjdhfkjdhfkjd fdsf fdsfdfqw434e 434324 34 34 34 3432432323 shfkdhdfkjdshdfkjdhf dhfdksjfhdksjfhkdsjfhdjsk ".getBytes());
                byte[]signatureBytes = rsa.sign();
                String encodeToString = Base64.getEncoder().encodeToString(signatureBytes);
                System.out.println("Signature:" + encodeToString);


            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        }


    public PrivateKey getPrivate(byte[] keyBytes) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public void sign(String data, PrivateKey privateKey) throws InvalidKeyException, Exception{
        Signature rsa = Signature.getInstance("SHA256withRSA");
        rsa.initSign(privateKey);
        rsa.update(data.getBytes());
        byte[]signatureBytes = rsa.sign();
        System.out.println("Signature:" + new BASE64Encoder().encode(signatureBytes));

    }

}
