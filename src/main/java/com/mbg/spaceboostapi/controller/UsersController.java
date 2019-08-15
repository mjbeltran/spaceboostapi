package com.mbg.spaceboostapi.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbg.spaceboostapi.dto.LoginRegisterDto;
import com.mbg.spaceboostapi.dto.UpdateUserDto;
import com.mbg.spaceboostapi.dto.UserDto;
import com.mbg.spaceboostapi.exception.InvalidRequestException;
import com.mbg.spaceboostapi.security.JwtService;
import com.mbg.spaceboostapi.service.EncryptService;
import com.mbg.spaceboostapi.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Users Management System")
public class UsersController {

	@Value("${default.image}")
	private String defaultImage;
	@Autowired
	private EncryptService encryptService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserService userService;

	@ApiOperation(value = "Create user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully user creation"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@RequestMapping(path = "/users", method = POST)
	public ResponseEntity<Object> createUser(@Valid @RequestBody LoginRegisterDto registerDto,
			BindingResult bindingResult) {
		checkInput(registerDto, bindingResult);

		UserDto user = new UserDto(registerDto.getUser().getEmail(), registerDto.getUser().getUsername(),
				encryptService.encrypt(registerDto.getUser().getPassword()), "", defaultImage, "");
		UserDto newUser = userService.saveUser(user);
		newUser.setToken(jwtService.toToken(user));
		return ResponseEntity.status(201).body(userResponse(newUser));
	}

	@ApiOperation(value = "Login user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully user login"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@RequestMapping(path = "/users/login", method = POST)
	public ResponseEntity<Object> userLogin(@RequestBody LoginRegisterDto loginDto, BindingResult bindingResult) {
		UserDto user = userService.findByEmail(loginDto.getUser().getEmail());
		if (user != null && encryptService.check(loginDto.getUser().getPassword(), user.getPassword())) {
			user.setToken(jwtService.toToken(user));
			return ResponseEntity.ok(userResponse(user));
		} else {
			bindingResult.rejectValue("user.password", "INVALID", "invalid email or password");
			throw new InvalidRequestException(bindingResult);
		}
	}

	@ApiOperation(value = "Get user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get user"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@GetMapping(path = "/user")
	public ResponseEntity<Object> currentUser(@AuthenticationPrincipal UserDto currentUser,
			@RequestHeader(value = "Authorization") String authorization) {
		UserDto user = userService.findById(currentUser.getId());
		user.setToken(authorization.split(" ")[1]);
		return ResponseEntity.ok(userResponse(user));
	}

	@ApiOperation(value = "Update user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully update user"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PutMapping(path = "/user")
	public ResponseEntity<Object> updateProfile(@AuthenticationPrincipal UserDto currentUser,
			@RequestHeader("Authorization") String token, @Valid @RequestBody UpdateUserDto updateUserDto,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
		UserDto user = checkAndUsertToUpdate(currentUser, token, updateUserDto, bindingResult);
		return ResponseEntity.ok(userResponse(user));
	}

	private UserDto checkAndUsertToUpdate(UserDto currentUser, String token, UpdateUserDto updateUserDto,
			BindingResult bindingResult) {
		checkUniquenessOfUsernameAndEmail(currentUser, updateUserDto, bindingResult);

		currentUser.update(updateUserDto.getEmail(), updateUserDto.getUsername(), updateUserDto.getPassword(),
				updateUserDto.getBio(), updateUserDto.getImage());
		userService.saveUser(currentUser);
		UserDto user = userService.findById(currentUser.getId());
		user.setToken(token.split(" ")[1]);
		return user;
	}

	private void checkUniquenessOfUsernameAndEmail(UserDto currentUser, UpdateUserDto updateUserParam,
			BindingResult bindingResult) {
		if (!"".equals(updateUserParam.getUsername())) {
			UserDto byUsername = userService.findByUsername(updateUserParam.getUsername());
			if (byUsername != null && !byUsername.equals(currentUser)) {
				bindingResult.rejectValue("user.username", "DUPLICATED", "username already exist");
			}
		}

		if (!"".equals(updateUserParam.getEmail())) {
			UserDto byEmail = userService.findByEmail(updateUserParam.getEmail());
			if (byEmail != null && !byEmail.equals(currentUser)) {
				bindingResult.rejectValue("user.email", "DUPLICATED", "email already exist");
			}
		}

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
	}

	private void checkInput(@Valid @RequestBody LoginRegisterDto registerDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
		if (userService.findByUsername(registerDto.getUser().getUsername()) != null) {
			bindingResult.rejectValue("user.username", "DUPLICATED", "duplicated username");
		}

		if (userService.findByEmail(registerDto.getUser().getEmail()) != null) {
			bindingResult.rejectValue("user.email", "DUPLICATED", "duplicated email");
		}

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
	}

	@SuppressWarnings("serial")
	private Map<String, Object> userResponse(UserDto userWithToken) {
		return new HashMap<String, Object>() {
			{
				put("user", userWithToken);
			}
		};
	}
}