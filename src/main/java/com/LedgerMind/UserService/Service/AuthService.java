package com.LedgerMind.UserService.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.LedgerMind.UserService.Security.JWTIssuer;
import com.LedgerMind.UserService.Security.UserPrincipal;
import com.LedgerMind.UserService.model.LoginResponse;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final JWTIssuer jwtIssuer;
	private final AuthenticationManager authenticationManager;
	
	
	public LoginResponse attemptlogin(String email, String password) {
		var authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		var principal = (UserPrincipal)authentication.getPrincipal();
		var roles = principal.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();
		var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);
		return new LoginResponse(token);
	}
}