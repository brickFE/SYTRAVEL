package com.sy.travel.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sy.travel.common.AjaxResult;

public class SYWebsocketServiceTest {

	@Test
	public void getMonitorShouldHandleNullTeamAndProductMapsIndependently() {
		SYProjectService projectService = mock(SYProjectService.class);
		SYTeamService teamService = mock(SYTeamService.class);
		SYProductService productService = mock(SYProductService.class);

		Map<String, Object> projectMap = new HashMap<>();
		List<Map<String, Object>> projectDocs = new ArrayList<>();
		projectMap.put("documents", projectDocs);

		when(projectService.queryAll("", 1, Integer.MAX_VALUE)).thenReturn(projectMap);
		when(teamService.findAll("", 1, Integer.MAX_VALUE)).thenReturn(null);
		when(productService.findAll("", 1, Integer.MAX_VALUE)).thenReturn(null);

		SYWebsocketService service = new SYWebsocketService();
		service.get(projectService, teamService, productService);

		AjaxResult<Map<String, Object>> result = service.getMonitor();
		assertEquals(projectDocs, result.getData().get("project"));
		assertTrue(result.getData().get("team") instanceof List);
		assertTrue(result.getData().get("product") instanceof List);
	}
}
