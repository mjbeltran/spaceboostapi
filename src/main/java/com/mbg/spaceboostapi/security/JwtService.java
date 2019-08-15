package com.mbg.spaceboostapi.security;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mbg.spaceboostapi.dto.UserDto;

@Service
public interface JwtService {
	String toToken(UserDto user);

	Optional<String> getSubFromToken(String token);
}