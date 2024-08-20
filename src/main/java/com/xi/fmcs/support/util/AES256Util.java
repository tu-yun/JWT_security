package com.xi.fmcs.support.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.xi.fmcs.support.model.AES256Const;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AES256Util {
		
    public String encode(String text) {
    	String cipherText = null;
    	try {
	    	AES256Const aes256Const = BeanUtil.search(AES256Const.class);
	    	String key = aes256Const.getKey();
	    	String iv = aes256Const.getIv();
	    	
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
	        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
	        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
	
	        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
	        cipherText = Base64.getEncoder().encodeToString(encrypted); 
    	} catch (Exception e) {
    		e.printStackTrace();
    		LogUtil.writeLog(e);
			return null;
		}
        return cipherText;
    }
    
    public String decode(String cipherText) {
    	String text = null;
    	try {
	    	AES256Const aes256Const = BeanUtil.search(AES256Const.class);
	    	String key = aes256Const.getKey();
	    	String iv = aes256Const.getIv();
	    	
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
	        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
	        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
	
	        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
	        byte[] decrypted = cipher.doFinal(decodedBytes);
	        text = new String(decrypted, "UTF-8");
    	} catch (Exception e) {
    		e.printStackTrace();
    		LogUtil.writeLog(e);
			return null;
		}
        return text;
    }
}
