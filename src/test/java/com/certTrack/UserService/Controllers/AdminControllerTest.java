package com.certTrack.UserService.Controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.LedgerMind.UserService.Repository.UserRepository;
import com.LedgerMind.UserService.model.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AdminControllerTest {

	@Autowired
	private MockMvc api;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ObjectMapper objectMapper;

	@Test
	void anyOneCanNotViewAdminEndpoint() throws Exception {
		api.perform(get("/admin")).
		andExpect(status().is4xxClientError());
	}
	
	@WithMockUser(auth = "ROLE_ADMIN")
	@Test
	void AdminCanViewAdminEndpoint() throws Exception {
		Object obj = userRepository.findAll();
		String responseJson = objectMapper.writeValueAsString(obj);
		api.perform(get("/admin/all")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(content().json(responseJson));
	}
	
	@WithMockUser(auth = "ROLE_ADMIN")
	@Test
	void AdminCanDeleteUser() throws Exception {
		ResponseMessage message = new ResponseMessage("no user by this id");
		String responseJson = objectMapper.writeValueAsString(message);
		api.perform(delete("/admin/delete?id=6")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(content().json(responseJson));
	}
}
