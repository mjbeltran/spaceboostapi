package com.mbg.spaceboostapi.service;

import org.springframework.stereotype.Service;

import com.mbg.spaceboostapi.dto.UserDto;

@Service
public interface UserService {

	public UserDto findById(String id);

	public UserDto saveUser(UserDto user);

	public UserDto findByUsername(String username);

	public UserDto findByEmail(String email);

}
