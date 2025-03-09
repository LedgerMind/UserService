package com.LedgerMind.UserService.Security;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JWTIssuer {
	
	@Autowired
	private JWTProperties properties;
	
	public String issue(long userId, String email, List<String> roles){
		return JWT.create()
				.withSubject(userId+"")
				.withExpiresAt(Instant.now().plus(Duration.of(1, ChronoUnit.DAYS)))
				.withClaim("e", email)
				.withClaim("a", roles)
				.sign(Algorithm.HMAC256(properties.getSecretKey()));
	}
}