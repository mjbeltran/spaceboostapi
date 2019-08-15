package com.mbg.spaceboostapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mbg.spaceboostapi.dto.UserDto;
import com.mbg.spaceboostapi.entities.User;
import com.mbg.spaceboostapi.repository.UserRepository;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDto findById(String id) {

		User user = userRepository.findById(Integer.parseInt(id));
		return UserDto.entityToDto(user);
	}

	@Override
	public UserDto saveUser(UserDto userDto) {
		userRepository.save(UserDto.dtoToEntity(userDto));
		return UserDto.entityToDto(userRepository.findByEmail(userDto.getEmail()));

	}

	@Override
	public UserDto findByUsername(String username) {

		return UserDto.entityToDto(userRepository.findByUsername(username));

	}

	@Override
	public UserDto findByEmail(String email) {
		return UserDto.entityToDto(userRepository.findByEmail(email));
	}

}