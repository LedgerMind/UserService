package com.LedgerMind.UserService.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.LedgerMind.UserService.Entity.User;
import com.LedgerMind.UserService.Repository.UserRepository;
import com.LedgerMind.UserService.model.ResponseMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public String saveUser(String name, String email, String password) {
		if(userRepository.findByEmail(email).isPresent()) {
			return "This user is already present!";
		}
		userRepository.save(
				User.builder()
					.name(name)
					.email(email)
					.password(passwordEncoder.encode(password))
					.currency("USD")
					.balance(0)
					.emailVerified(false)
					.createdAt(LocalDateTime.now())
					.role("ROLE_USER")
					.build());
		return "user successfully registered";
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	public String  delete(Long id) {
		if(userRepository.findById(id).isPresent()) {
			userRepository.deleteById(id);
			return "user successfully deleted";
		}
		return "no user by this id";
	}

	public ResponseMessage setRole(Long userId, String role) {
		User user = findById(userId);
		user.setRole(role);
		userRepository.save(user);
		return new ResponseMessage("user role successfully updated");
	}

	public ResponseEntity<ResponseMessage> updateUser(User user) {
		User currentUser = this.findById(user.getId());
		if (currentUser == null) {
		    return ResponseEntity.status(HttpStatus.NOT_FOUND)
		            .body(new ResponseMessage("user not found"));
		}
		currentUser.builder()
				.name(user.getName())
				.email(user.getEmail())
				.currency(user.getCurrency())
				.balance(user.getBalance())
				.build();
		return ResponseEntity.ok(new ResponseMessage("user updated succesfully"));
	}

	public ResponseEntity<?> updateUserPassword(Long id, String password) {
		User currentUser = this.findById(id);
		if (currentUser == null) {
		    return ResponseEntity.status(HttpStatus.NOT_FOUND)
		            .body(new ResponseMessage("user not found"));
		}
		currentUser.builder()
				.password(password)
				.build();
		return ResponseEntity.ok(new ResponseMessage("user updated succesfully"));
	}

}