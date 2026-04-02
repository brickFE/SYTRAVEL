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

import com.google.gson.Gson;
import com.sy.travel.common.AjaxResult;

@ServerEndpoint("/sy/refresh")
@Component
public class SYWebsocketService {
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
		scheduledService =  Executors.newSingleThreadScheduledExecutor();
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		if (scheduledService == null) {
			scheduledService = Executors.newSingleThreadScheduledExecutor();
		}
		if (monitorTask != null && !monitorTask.isCancelled()) {
			monitorTask.cancel(true);
		}
		int intervalMillis = resolveIntervalMillis(message);
		monitorTask = scheduledService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					Gson gson = new Gson();
					session.getBasicRemote().sendText(gson.toJson(getMonitor()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, intervalMillis, TimeUnit.MILLISECONDS);
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		if (monitorTask != null) {
			monitorTask.cancel(true);
		}
		if (scheduledService != null) {
			scheduledService.shutdownNow();
		}
	}
	
	@OnError
	public void onError(Throwable t) {
		if (monitorTask != null) {
			monitorTask.cancel(true);
		}
		if (scheduledService != null) {
			scheduledService.shutdownNow();
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
		Map<String,Object> projectMap = SYWebsocketService.sYProjectService.queryAll("",1, Integer.MAX_VALUE);
		if(projectMap == null) {
			resultMap.put("project", new ArrayList<>());
		} else {
			resultMap.put("project", (List<Map<String,Object>>)projectMap.get("documents"));
		}
		Map<String,Object> teamMap = SYWebsocketService.syTeamService.findAll("",1,Integer.MAX_VALUE);
		if(teamMap == null) {
			resultMap.put("team", new ArrayList<>());
		} else {
			resultMap.put("team", (List<Map<String,Object>>)teamMap.get("documents"));
		}
		Map<String,Object> productMap = SYWebsocketService.syProductService.findAll("", 1, Integer.MAX_VALUE);
		if(productMap == null) {
			resultMap.put("product", new ArrayList<>());
		} else {
			resultMap.put("product", (List<Map<String,Object>>)productMap.get("documents"));
		}
		return AjaxResult.success(resultMap);
	}
}
