package com.p6spy.engine.maskingtool;

import java.security.SecureRandom;

import javax.crypto.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class AesUtil {
	
	public static void main(String[] args) {
		String enStr = encrypt("832-17-1672", "832");
		System.out.println("enStr:"+enStr);
		
		String dedataStr = decrypt("eFbGkJmQkw4i2HtvD7zn4g==", "832");
		System.out.println("dedataStr:"+dedataStr);
	}
	
	/**
	 * AES加密
	 * @param content
	 * @param password
	 * @return
	 */
	public static String encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");//默认ECB模式
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return new Base64().encodeToString(result); // 加密
//            System.out.println(byteToHexString(result));
//            return byteToHexString(result);
        } catch (Exception e) {
                e.printStackTrace();
        }
        return null;
	}
	
	/**
	 * AES解密
	 * @param content
	 * @param password
	 * @return
	 */
	public static String decrypt(String content, String password) {
	    try {
        	KeyGenerator kgen = KeyGenerator.getInstance("AES");
         	kgen.init(128, new SecureRandom(password.getBytes()));
         	SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");            
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));
            return new String(result); // 加密
	    } catch (Exception e) {
	        e.printStackTrace();
	    } 
	    return null;
	}
	
	public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        String sTemp;
        for (int i = 0; i < bytes.length; i++) {
            sTemp = Integer.toHexString(0xFF & bytes[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
	}
}
