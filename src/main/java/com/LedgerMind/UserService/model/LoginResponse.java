package com.LedgerMind.UserService.model;

public class LoginResponse {
	private final String accestoken;

	public String getAccestoken() {
		return accestoken;
	}

	public LoginResponse(String accestoken) {
		super();
		this.accestoken = accestoken;
	}
	
}
