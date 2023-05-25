package com.example.demo.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Security;

public class EncodeUtil {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String MD5EncodeUtf8(String origin) {
        //盐
        String salt = "naklsdf8979asdml3248r09&**io3&*^";
//        origin = origin + PropertiesUtil.getProperty("password.salt", "");
        origin = origin + salt;

        return getMD5(origin).toUpperCase();
    }

    // 计算并获取CheckSum
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime, null);
    }

    // 计算并获取md5值
    private static String getMD5(String requestBody) {
        return encode("MD5", requestBody, "utf-8");
    }

    private static String encode(String algorithm, String value, String charsetname) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            if (charsetname == null || "".equals(charsetname)) {
                messageDigest.update(value.getBytes());
            } else {
                messageDigest.update(value.getBytes(charsetname));
            }
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (byte aByte : bytes) {
            buf.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return buf.toString();
    }

    public static String decodeBase64(String data) {
        byte[] resultByte;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            resultByte = decoder.decodeBuffer(data);
        } catch (IOException e) {
            return null;
        }
        return new String(resultByte);
    }

    /**
     * AES解密
     */
    public static String decryptData(String reqInfo, String key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        byte[] base64;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            base64 = decoder.decodeBuffer(reqInfo);
        } catch (IOException e) {
            return null;
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(getMD5(key).toLowerCase().getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return new String(cipher.doFinal(base64), StandardCharsets.UTF_8);
    }
}
