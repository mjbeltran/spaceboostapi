package com.mbg.spaceboostapi.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.mbg.spaceboostapi.entities.User;

public class UserDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -738635692064703412L;

	private String id;
	@NotBlank(message = "can't be empty")
	@Email(message = "should be an email")
	private String email;
	@NotBlank(message = "can't be empty")
	private String username;
	@NotBlank(message = "can't be empty")
	private String password;
	private String bio;
	private String image;
	private String token;

	public UserDto() {

	}

	public UserDto(String email, String username, String password, String bio, String image, String token) {
		this.id = "";
		this.email = email;
		this.username = username;
		this.password = password;
		this.bio = bio;
		this.image = image;
		this.token = token;
	}

	public void update(String email, String username, String password, String bio, String image) {
		if (!"".equals(email)) {
			this.email = email;
		}

		if (!"".equals(username)) {
			this.username = username;
		}

		if (!"".equals(password)) {
			this.password = password;
		}

		if (!"".equals(bio)) {
			this.bio = bio;
		}

		if (!"".equals(image)) {
			this.image = image;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public static User dtoToEntity(UserDto userdto) {

		User userEntity = null;
		if (userdto != null) {
			Mapper mapper = new DozerBeanMapper();
			userEntity = mapper.map(userdto, User.class);
		}
		return userEntity;
	}

	public static UserDto entityToDto(User user) {
		UserDto userDto = null;
		if (user != null) {
			Mapper mapper = new DozerBeanMapper();
			userDto = mapper.map(user, UserDto.class);
		}
		return userDto;
	}

}
