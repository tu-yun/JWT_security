package com.xi.fmcs.config.security.configuration;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder{
	
	@Override
	public String encode(CharSequence rawPassword){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(((String) rawPassword).getBytes(StandardCharsets.US_ASCII));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytesToHex(md.digest());
	}
	
    private String bytesToHex(byte[] bytes) {    	
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
	
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equals(encode(rawPassword));
	}

}
