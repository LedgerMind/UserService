package com.LedgerMind.UserService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LedgerMind.UserService.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);

}
