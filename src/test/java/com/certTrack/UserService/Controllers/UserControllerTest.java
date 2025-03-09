package com.certTrack.UserService.Controllers;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.LedgerMind.UserService.Entity.User;
import com.LedgerMind.UserService.Repository.UserRepository;
import com.LedgerMind.UserService.model.LoginRequest;
import com.LedgerMind.UserService.model.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerTest {

	@Autowired
	private MockMvc api;
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	UserRepository repository;

	@Test
	void anyOneCanViewPublicEndpoint() throws Exception {
		api.perform(get("/users/")).andExpect(status().isOk())
				.andExpect(content().string(containsStringIgnoringCase("USER SERVICE")));
	}

	@Test
	void anyOneCanRegister() throws Exception {
		LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
		String requestJson = objectMapper.writeValueAsString(loginRequest);
		
		ResponseMessage message = new ResponseMessage("This user is already present!");
		String responseJson = objectMapper.writeValueAsString(message);
		api.perform(post("/users/register")
				.contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isOk())
				.andExpect(content().json(responseJson));
	}

	@Test
	void notLogedInshouldnotseesecuredEndpoint() throws Exception {
		api.perform(get("/secured")).andExpect(status().is4xxClientError());
	}
	@Test
	@WithMockUser
	void AuthorizedUserShouldSeeSecuredEndpoint() throws Exception {
		api.perform(get("/users/secured")).andExpect(status().isOk())
				.andExpect(content().string(containsStringIgnoringCase("secuder ID: ")));
	}

	@Test
	void whileLoggingInShouldReceiveJWT() throws Exception {
		LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
		String requestJson = objectMapper.writeValueAsString(loginRequest);

		api.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accestoken").value(matchesPattern("^eyJhbGciOiJIUzI1N.*")));
	}

	@WithMockUser
	@Test
	void AuthorizedUserCanSeeInformationAboutUserById() throws Exception {
		User user = repository.findById(1L).get();
		String responseJson = objectMapper.writeValueAsString(user);
		api.perform(get("/users/user?id=1")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(content().json(responseJson));
	}

	@Test
	void NotAuthorizedUserCanNOTSeeInformationAboutUserById() throws Exception {
		api.perform(get("/users/user?id=1"))
			.andExpect(status()
			.is4xxClientError());
	}
}
