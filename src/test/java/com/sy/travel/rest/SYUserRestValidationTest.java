package com.sy.travel.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.sy.travel.service.SYUserService;

@RunWith(SpringRunner.class)
@WebMvcTest(SYUserRest.class)
public class SYUserRestValidationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SYUserService syUserService;

	@Test
	public void addShouldReturnBadRequestWhenUsernameMissing() throws Exception {
		String body = "{\"operator\":\"admin\",\"password\":\"123456\",\"permission\":\"0\"}";
		mockMvc.perform(post("/sy/user/add").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.msg").value("VALIDATION_ERROR"));
	}

	@Test
	public void updateShouldReturnBadRequestWhenIdMissing() throws Exception {
		String body = "{\"operator\":\"admin\"}";
		mockMvc.perform(post("/sy/user/update").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void addShouldReturnBadRequestWhenPermissionOutOfRange() throws Exception {
		String body = "{\"operator\":\"admin\",\"username\":\"u1\",\"password\":\"123456\",\"permission\":\"9\"}";
		mockMvc.perform(post("/sy/user/add").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void adminPwdShouldReturnBadRequestWhenNewPwdMissing() throws Exception {
		String body = "{\"operator\":\"admin\",\"password\":\"old\"}";
		mockMvc.perform(post("/sy/user/adminpwd").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void queryAllShouldReturnBadRequestWhenCurrentPageInvalid() throws Exception {
		mockMvc.perform(get("/sy/user/all").param("currentPage", "0"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void queryAllShouldReturnBadRequestWhenPageSizeInvalid() throws Exception {
		mockMvc.perform(get("/sy/user/all").param("pageSize", "0"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void deleteShouldReturnBadRequestWhenIdInvalid() throws Exception {
		mockMvc.perform(get("/sy/user/delete").param("id", "0").param("operator", "admin"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void deleteShouldReturnBadRequestWhenOperatorBlank() throws Exception {
		mockMvc.perform(get("/sy/user/delete").param("id", "1").param("operator", " "))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}
}
