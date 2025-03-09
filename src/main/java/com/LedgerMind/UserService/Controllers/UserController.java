package com.LedgerMind.UserService.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.LedgerMind.UserService.Entity.User;
import com.LedgerMind.UserService.Security.UserPrincipal;
import com.LedgerMind.UserService.Service.AuthService;
import com.LedgerMind.UserService.Service.UserService;
import com.LedgerMind.UserService.model.LoginRequest;
import com.LedgerMind.UserService.model.LoginResponse;
import com.LedgerMind.UserService.model.ResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;
	private final AuthService authService;
	
	
	@PostMapping("/register")
	public ResponseEntity<ResponseMessage> register(@RequestParam String name, 
			@RequestParam String email,
			@RequestParam String password) {
		String message = userService.saveUser(name, email, password);
		return ResponseEntity.ok(new ResponseMessage(message));	
	}

	
	
	@PostMapping("/auth/login")
	public LoginResponse login(@RequestBody @Validated LoginRequest loginRequest) {
		return authService.attemptlogin(loginRequest.getEmail(), loginRequest.getPassword());
	}
	
	
	@Operation(summary = "test endpoint", security = @SecurityRequirement(name = "basicScheme"), responses = {
	        @ApiResponse(description = "successful response", responseCode = "200")
	    })
	@GetMapping("/")
	public String start() {
		return "USER SERVICE";
	}
	
	
	@GetMapping("/profile")
	public User getUser(@RequestParam Long id) {
		return userService.findById(id);
	}
	@PutMapping("/profile")
	public ResponseEntity<?> updateUser(@RequestParam User user) {
		return userService.updateUser(user);
	}
	
	
	@PutMapping("/change-password")
	public ResponseEntity<?> updateUserPassword(@RequestParam Long id, @RequestParam String password) {
		return userService.updateUserPassword(id, password);
	}

	
	//endpoint for test is user authorized
	@GetMapping("/secured")
	public String authTest(@AuthenticationPrincipal UserPrincipal principal) { 
		return "secuder ID: "+principal.getUserId()+" " +principal.getEmail();
	}
	
	
	
	
}