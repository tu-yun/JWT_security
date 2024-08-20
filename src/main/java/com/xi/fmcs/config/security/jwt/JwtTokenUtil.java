package com.xi.fmcs.config.security.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.xi.fmcs.support.util.AES256Util;
import com.xi.fmcs.config.security.model.JwtConst;
import com.xi.fmcs.domain.admin.model.AdminMemberLoginDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
	
	private final JwtConst jwtConst;

	public String createAccessToken(AdminMemberLoginDto adminMemberLoginDto, Date issuedAt) {
		String pkA = null;
		String accessToken = null;
		try {
			pkA = AES256Util.encode(adminMemberLoginDto.getEmail());
			if(pkA != null && !pkA.trim().equals("")) {
				accessToken = JWT.create()
						.withSubject(jwtConst.getSubject())
						.withClaim("pkA", pkA)
						.withIssuedAt(issuedAt)
						.withExpiresAt(new Date(System.currentTimeMillis() + jwtConst.getAccess().getExpireTime()))
						.sign(Algorithm.HMAC512(jwtConst.getAccess().getSecretKey()));
			} else {
				return null;
			}
		} catch (Exception e) {
    		e.printStackTrace();
    		LogUtil.writeLog(e);
			return null;
		}
		return accessToken;
	}
	
	public String createRefreshToken(AdminMemberLoginDto adminMemberLoginDto, Date issuedAt) {
		String pkR = null;
		String refreshToken = null;
		try {
			pkR = AES256Util.encode(adminMemberLoginDto.getEmail());
			if(pkR != null && !pkR.trim().equals("")) {
				refreshToken = JWT.create()				
						.withSubject(jwtConst.getSubject())
						.withClaim("pkR", pkR)
						.withIssuedAt(issuedAt)
						.withExpiresAt(new Date(System.currentTimeMillis() + jwtConst.getRefresh().getExpireTime()))
						.sign(Algorithm.HMAC512(jwtConst.getRefresh().getSecretKey()));
			} else {
				return null;
			}
		} catch (Exception e) {
    		e.printStackTrace();
    		LogUtil.writeLog(e);
			return null;
		}
		return refreshToken;
	}
	
}
