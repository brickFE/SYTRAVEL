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

import com.sy.travel.service.SYProjectService;

@RunWith(SpringRunner.class)
@WebMvcTest(SYProjectRest.class)
public class SYProjectRestValidationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SYProjectService syProjectService;

	@Test
	public void addShouldReturnBadRequestWhenCodeMissing() throws Exception {
		String body = "{\"operator\":\"admin\",\"name\":\"p1\",\"beginDate\":\"2026-01-01 00:00:00\",\"endDate\":\"2026-01-02 00:00:00\",\"valid\":\"1\"}";
		mockMvc.perform(post("/sy/project/add").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}
}
