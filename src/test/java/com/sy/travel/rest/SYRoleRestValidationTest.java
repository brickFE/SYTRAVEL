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

import com.sy.travel.service.SYRoleService;

@RunWith(SpringRunner.class)
@WebMvcTest(SYRoleRest.class)
public class SYRoleRestValidationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SYRoleService syRoleService;

	@Test
	public void addShouldReturnBadRequestWhenNameMissing() throws Exception {
		String body = "{\"operator\":\"admin\",\"teamId\":\"1\",\"role\":\"dev\",\"mobile\":\"13800000000\"}";
		mockMvc.perform(post("/sy/role/add").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void updateShouldReturnBadRequestWhenIdMissing() throws Exception {
		String body = "{\"operator\":\"admin\"}";
		mockMvc.perform(post("/sy/role/update").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void queryAllShouldReturnBadRequestWhenCurrentPageInvalid() throws Exception {
		mockMvc.perform(get("/sy/role/all").param("currentPage", "0"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}
}
