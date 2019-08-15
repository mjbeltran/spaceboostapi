package com.mbg.spaceboostapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptServiceImpl implements EncryptService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public String encrypt(String password) {
		// return bCryptPasswordEncoder.encode(password);
		return password;
	}

	@Override
	public boolean check(String checkPassword, String realPassword) {
		// return bCryptPasswordEncoder.encode(checkPassword).equals(realPassword);
		return checkPassword.equals(realPassword);
	}
}
