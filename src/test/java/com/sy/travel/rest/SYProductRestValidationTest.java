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

import com.sy.travel.service.SYProductService;

@RunWith(SpringRunner.class)
@WebMvcTest(SYProductRest.class)
public class SYProductRestValidationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SYProductService syProductService;

	@Test
	public void addShouldReturnBadRequestWhenCodeMissing() throws Exception {
		String body = "{\"operator\":\"admin\",\"name\":\"p\",\"teamId\":\"1\",\"onlineDate\":\"2026-01-01 00:00:00\",\"offlineDate\":\"2026-01-02 00:00:00\",\"classId\":\"1\",\"quantity\":1,\"minQty\":1,\"soldQty\":0,\"price\":1,\"nights\":1,\"status\":\"1\"}";
		mockMvc.perform(post("/sy/product/add").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void updateShouldReturnBadRequestWhenIdMissing() throws Exception {
		String body = "{\"operator\":\"admin\",\"quantity\":1,\"minQty\":1,\"soldQty\":0,\"price\":1,\"nights\":1,\"status\":\"1\"}";
		mockMvc.perform(post("/sy/product/update").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void queryAllShouldReturnBadRequestWhenCurrentPageInvalid() throws Exception {
		mockMvc.perform(get("/sy/product/all").param("currentPage", "0"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void queryAllShouldReturnBadRequestWhenPageSizeInvalid() throws Exception {
		mockMvc.perform(get("/sy/product/all").param("pageSize", "0"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void deleteShouldReturnBadRequestWhenIdInvalid() throws Exception {
		mockMvc.perform(get("/sy/product/delete").param("id", "0").param("operator", "admin"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void queryByTeamIdShouldReturnBadRequestWhenIdInvalid() throws Exception {
		mockMvc.perform(get("/sy/product/teamid").param("id", "0"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}

	@Test
	public void queryByClassIdShouldReturnBadRequestWhenIdInvalid() throws Exception {
		mockMvc.perform(get("/sy/product/classid").param("id", "0"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
	}
}
