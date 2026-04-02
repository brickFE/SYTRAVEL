package com.sy.travel.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import jakarta.websocket.Session;

import com.sy.travel.common.AjaxResult;

public class SYWebsocketServiceTest {

	@Test
	public void resolveIntervalMillisShouldUseDefaultWhenInvalid() {
		SYWebsocketService service = new SYWebsocketService();
		assertEquals(1000, service.resolveIntervalMillis("abc"));
	}

	@Test
	public void resolveIntervalMillisShouldClampTooSmallValues() {
		SYWebsocketService service = new SYWebsocketService();
		assertEquals(100, service.resolveIntervalMillis("1"));
	}

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

	@Test
	public void getMonitorShouldReturnEmptyListsWhenServicesUnavailable() throws Exception {
		SYWebsocketService service = new SYWebsocketService();
		Object oldProjectService = getStaticField(SYWebsocketService.class, "sYProjectService");
		Object oldTeamService = getStaticField(SYWebsocketService.class, "syTeamService");
		Object oldProductService = getStaticField(SYWebsocketService.class, "syProductService");
		try {
			setStaticField(SYWebsocketService.class, "sYProjectService", null);
			setStaticField(SYWebsocketService.class, "syTeamService", null);
			setStaticField(SYWebsocketService.class, "syProductService", null);

			AjaxResult<Map<String, Object>> result = service.getMonitor();
			assertTrue(result.getData().get("project") instanceof List);
			assertTrue(((List<?>) result.getData().get("project")).isEmpty());
			assertTrue(result.getData().get("team") instanceof List);
			assertTrue(((List<?>) result.getData().get("team")).isEmpty());
			assertTrue(result.getData().get("product") instanceof List);
			assertTrue(((List<?>) result.getData().get("product")).isEmpty());
		} finally {
			setStaticField(SYWebsocketService.class, "sYProjectService", oldProjectService);
			setStaticField(SYWebsocketService.class, "syTeamService", oldTeamService);
			setStaticField(SYWebsocketService.class, "syProductService", oldProductService);
		}
	}

	@Test
	public void getMonitorShouldReturnEmptyListsWhenDocumentsMissing() {
		SYProjectService projectService = mock(SYProjectService.class);
		SYTeamService teamService = mock(SYTeamService.class);
		SYProductService productService = mock(SYProductService.class);

		when(projectService.queryAll("", 1, Integer.MAX_VALUE)).thenReturn(new HashMap<String, Object>());
		when(teamService.findAll("", 1, Integer.MAX_VALUE)).thenReturn(new HashMap<String, Object>());
		when(productService.findAll("", 1, Integer.MAX_VALUE)).thenReturn(new HashMap<String, Object>());

		SYWebsocketService service = new SYWebsocketService();
		service.get(projectService, teamService, productService);

		AjaxResult<Map<String, Object>> result = service.getMonitor();
		assertTrue(((List<?>) result.getData().get("project")).isEmpty());
		assertTrue(((List<?>) result.getData().get("team")).isEmpty());
		assertTrue(((List<?>) result.getData().get("product")).isEmpty());
	}

	@Test
	public void getMonitorShouldReturnEmptyWhenDocumentsTypeIsInvalid() {
		SYProjectService projectService = mock(SYProjectService.class);
		SYTeamService teamService = mock(SYTeamService.class);
		SYProductService productService = mock(SYProductService.class);

		Map<String, Object> invalid = new HashMap<>();
		invalid.put("documents", "invalid");
		when(projectService.queryAll("", 1, Integer.MAX_VALUE)).thenReturn(invalid);
		when(teamService.findAll("", 1, Integer.MAX_VALUE)).thenReturn(invalid);
		when(productService.findAll("", 1, Integer.MAX_VALUE)).thenReturn(invalid);

		SYWebsocketService service = new SYWebsocketService();
		service.get(projectService, teamService, productService);

		AjaxResult<Map<String, Object>> result = service.getMonitor();
		assertTrue(((List<?>) result.getData().get("project")).isEmpty());
		assertTrue(((List<?>) result.getData().get("team")).isEmpty());
		assertTrue(((List<?>) result.getData().get("product")).isEmpty());
	}

	@Test
	public void onCloseShouldCancelTaskAndShutdownScheduler() throws Exception {
		SYWebsocketService service = new SYWebsocketService();
		ScheduledFuture<?> future = mock(ScheduledFuture.class);
		ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
		setField(service, "monitorTask", future);
		setField(service, "scheduledService", executorService);

		service.onClose(null, null);

		verify(future).cancel(true);
		verify(executorService).shutdownNow();
		assertEquals(null, getField(service, "monitorTask"));
		assertEquals(null, getField(service, "scheduledService"));
	}

	@Test
	public void onMessageShouldCancelTaskWhenSessionClosed() throws Exception {
		SYWebsocketService service = new SYWebsocketService();
		ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
		ScheduledFuture<?> previousTask = mock(ScheduledFuture.class);
		ScheduledFuture<?> newTask = mock(ScheduledFuture.class);
		Session session = mock(Session.class);

		when(session.isOpen()).thenReturn(false);
		when(executorService.scheduleAtFixedRate(org.mockito.ArgumentMatchers.any(Runnable.class), anyLong(), anyLong(),
				eq(TimeUnit.MILLISECONDS))).thenReturn(newTask);

		setField(service, "scheduledService", executorService);
		setField(service, "monitorTask", previousTask);

		service.onMessage("1000", session);

		verify(previousTask).cancel(true);

		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		verify(executorService).scheduleAtFixedRate(captor.capture(), eq(0L), eq(1000L), eq(TimeUnit.MILLISECONDS));
		captor.getValue().run();

		verify(newTask).cancel(true);
		verify(session, never()).getBasicRemote();
	}

	private void setField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private Object getField(Object target, String fieldName) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(target);
	}

	private void setStaticField(Class<?> type, String fieldName, Object value) throws Exception {
		Field field = type.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(null, value);
	}

	private Object getStaticField(Class<?> type, String fieldName) throws Exception {
		Field field = type.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(null);
	}
}
