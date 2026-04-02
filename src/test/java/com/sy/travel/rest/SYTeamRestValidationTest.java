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

import com.sy.travel.service.SYTeamService;

@RunWith(SpringRunner.class)
@WebMvcTest(SYTeamRest.class)
public class SYTeamRestValidationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SYTeamService syTeamService;

	@Test
	public void addShouldReturnBadRequestWhenNameMissing() throws Exception {
		String body = "{\"operator\":\"admin\",\"projectId\":\"1\",\"valid\":\"1\"}";
		mockMvc.perform(post("/sy/team/add").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void updateShouldReturnBadRequestWhenIdMissing() throws Exception {
		String body = "{\"operator\":\"admin\"}";
		mockMvc.perform(post("/sy/team/update").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void findAllShouldReturnBadRequestWhenCurrentPageInvalid() throws Exception {
		mockMvc.perform(get("/sy/team/all").param("currentPage", "0"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void findAllShouldReturnBadRequestWhenPageSizeInvalid() throws Exception {
		mockMvc.perform(get("/sy/team/all").param("pageSize", "0"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void deleteByTeamIdShouldReturnBadRequestWhenIdInvalid() throws Exception {
		mockMvc.perform(get("/sy/team/delete").param("id", "0").param("operator", "admin"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void deleteByTeamIdShouldReturnBadRequestWhenOperatorBlank() throws Exception {
		mockMvc.perform(get("/sy/team/delete").param("id", "1").param("operator", " "))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void infoShouldReturnBadRequestWhenIdInvalid() throws Exception {
		mockMvc.perform(get("/sy/team/info").param("id", "0"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}
}
