package com.consoleolog.anyaskapiserver.v1.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import javax.crypto.Cipher;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
public class CryptoHandler {

    private final static String shaAlg = "SHA-256";
    private final static String aesAlg = "AES/CBC/PKCS5Padding";
    private final static String key = "MyTestCode-32Character-TestAPIKey";
    private final static String alg = "AES/CBC/PKCS5Padding";
    private final static String iv = key.substring(0, 16);

    public String decrypt(String clientKey) throws Exception {
        Cipher cp = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cp.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

        byte[] decodeBytes = Base64.getDecoder().decode(clientKey.getBytes());
        byte[] decrypted = cp.doFinal(decodeBytes);
        return new String(decrypted).trim();
    }

    public String encrypt(String text) throws Exception {
        String shaKey = getShaKey(text);
        return getAesKey(shaKey);
    }
    private String getAesKey(String text) throws Exception {
        Cipher cp = Cipher.getInstance(aesAlg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

        byte[] encodedBytes = cp.doFinal(text.getBytes());
        byte[] encrypted = Base64.getEncoder().encode(encodedBytes);
        return new String(encrypted).trim();
    }
    private String getShaKey(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(shaAlg);
        md.update(text.getBytes());
        return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
