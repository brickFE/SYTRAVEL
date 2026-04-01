package com.sy.travel.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.sy.travel.service.SYLoginService;

@RunWith(SpringRunner.class)
@WebMvcTest(SYLoginRest.class)
public class SYLoginRestValidationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SYLoginService syLoginService;

	@Test
	public void loginShouldReturnBadRequestWhenUsernameMissing() throws Exception {
		String body = "{\"password\":\"123456\"}";
		mockMvc.perform(post("/sy/login/check").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void loginShouldReturnBadRequestWhenPasswordMissing() throws Exception {
		String body = "{\"username\":\"admin\"}";
		mockMvc.perform(post("/sy/login/check").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400));
	}
}
