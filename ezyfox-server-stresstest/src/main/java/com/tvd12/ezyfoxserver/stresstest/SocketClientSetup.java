package com.tvd12.ezyfoxserver.stresstest;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandlers;
import com.tvd12.ezyfoxserver.client.manager.EzyHandlerManager;
import com.tvd12.ezyfoxserver.stresstest.handler.AccessAppHandler;
import com.tvd12.ezyfoxserver.stresstest.handler.HandshakeHandler;
import com.tvd12.ezyfoxserver.stresstest.handler.LoginSuccessHandler;
import com.tvd12.ezyfoxserver.stresstest.handler.UdpHandshakeHandler;

public class SocketClientSetup {

	private final AtomicInteger counter = new AtomicInteger();
	private final AtomicLong messageCount = new AtomicLong();
	private final ScheduledExecutorService executorService;
	
	public SocketClientSetup(String clientType) {
		this.executorService = EzyExecutors.newScheduledThreadPool(5, clientType + "-message-schedule");
		Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdown));
	}
	
	public void setup(EzyClient client, boolean useUdp) {
		int count = counter.incrementAndGet();
		EzyHandlerManager handlerManager = client.getHandlerManager();
		handlerManager.addDataHandler(EzyCommand.HANDSHAKE, new HandshakeHandler(count));
		handlerManager.addDataHandler(EzyCommand.LOGIN, new LoginSuccessHandler(useUdp));
		handlerManager.addDataHandler(EzyCommand.UDP_HANDSHAKE, new UdpHandshakeHandler());
		handlerManager.addDataHandler(EzyCommand.APP_ACCESS, new AccessAppHandler(useUdp, messageCount, executorService));
		
		EzyAppDataHandlers appDataHandlers = handlerManager.getAppDataHandlers("hello-world");
		appDataHandlers.addHandler("broadcastMessage", (app, data) -> {
			String message = ((EzyObject)data).get("message", String.class);
			System.out.println("tcp > server response: " + message);
		});
		appDataHandlers.addHandler("udpBroadcastMessage", (app, data) -> {
			String message = ((EzyObject)data).get("message", String.class);
			System.out.println("udp > server response: " + message);
		});
	}
	
}
