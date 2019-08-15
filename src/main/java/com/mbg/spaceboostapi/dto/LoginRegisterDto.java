package com.mbg.spaceboostapi.dto;

import java.io.Serializable;

import javax.validation.Valid;

public class LoginRegisterDto implements Serializable {

	@Valid
	private UserDto user;
	/**
	 *
	 */
	private static final long serialVersionUID = -6028505404963405635L;

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

}