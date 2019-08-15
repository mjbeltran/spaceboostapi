package com.mbg.spaceboostapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbg.spaceboostapi.entities.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
	User findById(int id);

	User findByEmail(String email);

	User findByUsername(String username);
}