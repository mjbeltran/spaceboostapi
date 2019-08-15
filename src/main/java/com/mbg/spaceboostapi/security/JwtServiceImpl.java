package com.mbg.spaceboostapi.security;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mbg.spaceboostapi.dto.UserDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtServiceImpl implements JwtService {
	@Value("${jwt.secretkey}")
	private String secret;
	@Value("${jwt.timeSession}")
	private int sessionTime;

	@Override
	public String toToken(UserDto user) {
		return Jwts.builder().setSubject(user.getId()).setExpiration(expireTimeFromNow())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	@Override
	public Optional<String> getSubFromToken(String token) {
		try {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return Optional.ofNullable(claimsJws.getBody().getSubject());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	private Date expireTimeFromNow() {
		return new Date(System.currentTimeMillis() + sessionTime * 1000);
	}
}
