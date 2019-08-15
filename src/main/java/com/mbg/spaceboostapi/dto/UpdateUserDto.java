package com.mbg.spaceboostapi.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user")
public class UpdateUserDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -247468407750652230L;
	@Email(message = "should be an email")
	private String email = "";
	private String password = "";
	private String username = "";
	private String bio = "";
	private String image = "";
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
}
