package com.mbg.spaceboostapi;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbg.spaceboostapi.controller.UsersController;
import com.mbg.spaceboostapi.dto.UserDto;
import com.mbg.spaceboostapi.repository.UserRepository;
import com.mbg.spaceboostapi.security.JwtService;
import com.mbg.spaceboostapi.security.WebSecurityConfig;
import com.mbg.spaceboostapi.service.EncryptServiceImpl;
import com.mbg.spaceboostapi.service.UserService;
import com.mbg.spaceboostapi.service.UserServiceImpl;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersController.class)
@Import({ WebSecurityConfig.class, UserServiceImpl.class, EncryptServiceImpl.class })
public class UsersControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private JwtService jwtService;

	@MockBean
	private UserService userService;
	private String defaultAvatar;

	@Before
	public void setUp() throws Exception {
		RestAssuredMockMvc.mockMvc(mvc);
		defaultAvatar = "https://www.w3schools.com/images/picture.jpg";
	}

	@Test
	public void createUserTest() throws Exception {
		String email = "mbg@gmail.com";
		String username = "mjbeltran";

		when(jwtService.toToken(any())).thenReturn("123");
		UserDto user = new UserDto(email, username, "123", "", defaultAvatar, "");
		when(userService.findById(any())).thenReturn(user);
		when(userService.findByUsername(eq(username))).thenReturn(null);
		when(userService.findByEmail(eq(email))).thenReturn(null);
		when(userService.saveUser(any())).thenReturn(user);

		Map<String, Object> param = prepareRegisterParameter(email, username);

		given().contentType("application/json").body(param).when().post("/users").then().statusCode(201)
		.body("user.email", equalTo(email)).body("user.username", equalTo(username))
		.body("user.bio", equalTo("")).body("user.image", equalTo(defaultAvatar))
		.body("user.token", equalTo("123"));

		verify(userService).saveUser(any());
	}

	/*
	 * converts a Java object into JSON representation
	 */
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void emptyUsername() throws Exception {

		String email = "mbg@gmail.com";
		String username = "";

		Map<String, Object> param = prepareRegisterParameter(email, username);

		given()
		.contentType("application/json")
		.body(param)
		.when()
		.post("/users")
		.then()
		.statusCode(422)
		.body("attributeErrors.message[0]", equalTo("can't be empty"));
	}

	@Test
	public void loginOKTest() throws Exception {
		String email = "mbg@gmail.com";
		String username = "mjbeltran";
		String password = "123";

		UserDto user = new UserDto(email, username, password, "", defaultAvatar,"");

		when(userService.findByEmail(eq(email))).thenReturn(user);
		when(userService.findByUsername(eq(username))).thenReturn(user);
		when(userService.findById(eq(user.getId()))).thenReturn(user);
		when(jwtService.toToken(any())).thenReturn("123");

		Map<String, Object> param = new HashMap<String, Object>() {{
			put("user", new HashMap<String, Object>() {{
				put("email", email);
				put("password", password);
			}});
		}};

		given()
		.contentType("application/json")
		.body(param)
		.when()
		.post("/users/login")
		.then()
		.statusCode(200)
		.body("user.email", equalTo(email))
		.body("user.username", equalTo(username))
		.body("user.bio", equalTo(""))
		.body("user.image", equalTo(defaultAvatar))
		.body("user.token", equalTo("123"));;
	}

	@Test
	public void loginKoPassword() throws Exception {
		String email = "mbg@gmail.com";
		String username = "mjbeltran";
		String password = "123";

		UserDto user = new UserDto(email, username, password, "", defaultAvatar,"");

		when(userService.findByEmail(eq(email))).thenReturn(user);
		when(userService.findByUsername(eq(username))).thenReturn(user);

		Map<String, Object> param = new HashMap<String, Object>() {{
			put("user", new HashMap<String, Object>() {{
				put("email", email);
				put("password", "123123");
			}});
		}};

		given()
		.contentType("application/json")
		.body(param)
		.when()
		.post("/users/login")
		.then()
		.statusCode(422)
		.body("attributeErrors.message[0]", equalTo("invalid email or password"));
	}

	@SuppressWarnings("serial")
	private HashMap<String, Object> prepareRegisterParameter(final String email, final String username) {
		return new HashMap<String, Object>() {
			{
				put("user", new HashMap<String, Object>() {
					{
						put("email", email);
						put("password", "1234");
						put("username", username);
					}
				});
			}
		};
	}
}
