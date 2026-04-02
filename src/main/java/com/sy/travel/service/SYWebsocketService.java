package com.sy.travel.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.sy.travel.common.AjaxResult;

@ServerEndpoint("/sy/refresh")
@Component
public class SYWebsocketService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SYWebsocketService.class);
	private static SYProjectService sYProjectService;
	private static SYTeamService syTeamService;
	private static SYProductService syProductService;
	private ScheduledExecutorService scheduledService;
	private ScheduledFuture<?> monitorTask;
	@Autowired
	public void get(SYProjectService sYProjectService, SYTeamService syTeamService, SYProductService syProductService) {
		SYWebsocketService.sYProjectService = sYProjectService;
		SYWebsocketService.syTeamService = syTeamService;
		SYWebsocketService.syProductService = syProductService;
	}
	@OnOpen
	public void onOpen(Session session){
		if (scheduledService == null || scheduledService.isShutdown()) {
			scheduledService = Executors.newSingleThreadScheduledExecutor();
		}
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		if (scheduledService == null || scheduledService.isShutdown()) {
			scheduledService = Executors.newSingleThreadScheduledExecutor();
		}
		cancelMonitorTask();
		int intervalMillis = resolveIntervalMillis(message);
		monitorTask = scheduledService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (session == null || !session.isOpen()) {
					cancelMonitorTask();
					return;
				}
				try {
					Gson gson = new Gson();
					session.getBasicRemote().sendText(gson.toJson(getMonitor()));
				} catch (IOException | IllegalStateException e) {
					LOGGER.warn("websocket monitor push failed", e);
					cancelMonitorTask();
				}
			}
		}, 0, intervalMillis, TimeUnit.MILLISECONDS);
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		stopMonitor();
	}
	
	@OnError
	public void onError(Throwable t) {
		stopMonitor();
	}

	private void stopMonitor() {
		cancelMonitorTask();
		if (scheduledService != null) {
			scheduledService.shutdownNow();
		}
	}

	private void cancelMonitorTask() {
		if (monitorTask != null) {
			monitorTask.cancel(true);
		}
	}

	int resolveIntervalMillis(String message) {
		try {
			int value = Integer.parseInt(message);
			return Math.max(100, value);
		} catch (NumberFormatException ex) {
			return 1000;
		}
	}
	
	public AjaxResult<Map<String, Object>> getMonitor(){
		Map<String,Object> resultMap = new HashMap<>();
		if (SYWebsocketService.sYProjectService == null) {
			LOGGER.warn("project service is unavailable, fallback to empty monitor data");
			resultMap.put("project", new ArrayList<>());
		} else {
			Map<String,Object> projectMap = SYWebsocketService.sYProjectService.queryAll("",1, Integer.MAX_VALUE);
			resultMap.put("project", extractDocuments(projectMap));
		}
		if (SYWebsocketService.syTeamService == null) {
			LOGGER.warn("team service is unavailable, fallback to empty monitor data");
			resultMap.put("team", new ArrayList<>());
		} else {
			Map<String,Object> teamMap = SYWebsocketService.syTeamService.findAll("",1,Integer.MAX_VALUE);
			resultMap.put("team", extractDocuments(teamMap));
		}
		if (SYWebsocketService.syProductService == null) {
			LOGGER.warn("product service is unavailable, fallback to empty monitor data");
			resultMap.put("product", new ArrayList<>());
		} else {
			Map<String,Object> productMap = SYWebsocketService.syProductService.findAll("", 1, Integer.MAX_VALUE);
			resultMap.put("product", extractDocuments(productMap));
		}
		return AjaxResult.success(resultMap);
	}

	private List<Map<String, Object>> extractDocuments(Map<String, Object> source) {
		if (source == null) {
			return new ArrayList<>();
		}
		Object documents = source.get("documents");
		if (documents instanceof List) {
			return (List<Map<String, Object>>) documents;
		}
		return new ArrayList<>();
	}
}
