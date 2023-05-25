package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName OilRedis
 * @Description Hello World!
 * @Author tchuhu
 * @Date 2021/4/13 14:32
 * @Version 1.0
 */
@Component
public class OilRedis {
    private Logger logger = LoggerFactory.getLogger(this.getClass());



    public static SecretKeySpec generateMySQLAESKey(final String key, final String encoding) {
        try {
            final byte[] finalKey = new byte[16];
            int i = 0;
            for(byte b : key.getBytes(encoding))
                finalKey[i++%16] ^= b;
            return new SecretKeySpec(finalKey, "AES");
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String  encodeHex(byte[] b){
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int i = 0; i < b.length; i++) {
            stmp = Integer.toHexString(b[i] & 0xFF).toUpperCase();
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString();

    }
    public static String aesEncrypt(Object pass, String strKey) {
        if(pass==null){
            return null;
        }
        String password = pass.toString();
        try {
            SecretKey key = generateMySQLAESKey(strKey,"ASCII");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = password.getBytes("UTF-8");
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return encodeHex(ciphertextBytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }



}
