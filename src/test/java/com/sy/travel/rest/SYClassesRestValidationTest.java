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

import com.sy.travel.service.SYClassesService;

@RunWith(SpringRunner.class)
@WebMvcTest(SYClassesRest.class)
public class SYClassesRestValidationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SYClassesService syClassesService;

	@Test
	public void addShouldReturnBadRequestWhenNameMissing() throws Exception {
		String body = "{\"operator\":\"admin\",\"sortId\":\"1\"}";
		mockMvc.perform(post("/sy/classes/add").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void updateShouldReturnBadRequestWhenIdMissing() throws Exception {
		String body = "{\"operator\":\"admin\"}";
		mockMvc.perform(post("/sy/classes/update").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void queryAllShouldReturnBadRequestWhenCurrentPageInvalid() throws Exception {
		mockMvc.perform(get("/sy/classes/all").param("currentPage", "0"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void queryAllShouldReturnBadRequestWhenPageSizeInvalid() throws Exception {
		mockMvc.perform(get("/sy/classes/all").param("pageSize", "0"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void deleteShouldReturnBadRequestWhenIdInvalid() throws Exception {
		mockMvc.perform(get("/sy/classes/delete").param("id", "0").param("operator", "admin"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}
}
