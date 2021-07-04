package com.example.simple_chat.client;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.util.EzyEntityObjects;
import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.EzyClients;
import com.tvd12.ezyfoxserver.client.EzyUTClient;
import com.tvd12.ezyfoxserver.client.config.EzyClientConfig;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.entity.EzyApp;
import com.tvd12.ezyfoxserver.client.event.EzyEventType;
import com.tvd12.ezyfoxserver.client.handler.EzyAppAccessHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyConnectionFailureHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyConnectionSuccessHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyHandshakeHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyLoginSuccessHandler;
import com.tvd12.ezyfoxserver.client.request.EzyAppAccessRequest;
import com.tvd12.ezyfoxserver.client.request.EzyLoginRequest;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.setup.EzyAppSetup;
import com.tvd12.ezyfoxserver.client.setup.EzySetup;
import com.tvd12.ezyfoxserver.client.socket.EzyMainEventsLoop;

public class SimpleChatClient {
	
	private EzyClient socketClient;
	private static final String ZONE_NAME = "example";
	private static final String APP_NAME = "simple-chat";

	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = 3005;
		if(args.length > 0)
			host = args[0];
		if(args.length > 1)
			port = Integer.parseInt(args[1]);
		SimpleChatClient client = new SimpleChatClient();
		client.connect(host, port);
		EzyMainEventsLoop mainEventsLoop = new EzyMainEventsLoop();
		mainEventsLoop.start();
	}
	
	public SimpleChatClient() {
		this.socketClient = setup();
	}
	
	protected EzyClient setup() {
		EzyClientConfig clientConfig = EzyClientConfig.builder()
				.zoneName(ZONE_NAME)
				.build();
		EzyClients clients = EzyClients.getInstance();
		EzyClient client = new EzyUTClient(clientConfig);
		clients.addClient(client);
		EzySetup setup = client.setup();
		setup.addEventHandler(EzyEventType.CONNECTION_SUCCESS, new EzyConnectionSuccessHandler());
		setup.addEventHandler(EzyEventType.CONNECTION_FAILURE, new EzyConnectionFailureHandler());
		setup.addDataHandler(EzyCommand.HANDSHAKE, new ExHandshakeEventHandler());
		setup.addDataHandler(EzyCommand.LOGIN, new ExLoginSuccessHandler());
		setup.addDataHandler(EzyCommand.APP_ACCESS, new ExAccessAppHandler());

		EzyAppSetup appSetup = setup.setupApp(APP_NAME);
		appSetup.addDataHandler("chat/sendMessage", new ChatSendMessageResponseHandler());
		appSetup.addDataHandler("greet", new ChatGreetResponseHandler());
		appSetup.addDataHandler("hello", new ChatHelloResponseHandler());
		return client;
	}
	
	public void connect(String host, int port) {
		socketClient.connect(host, port);
	}
	
	class ExHandshakeEventHandler extends EzyHandshakeHandler {
		@Override
		protected EzyRequest getLoginRequest() {
			return new EzyLoginRequest(ZONE_NAME, "Dzung", "123456");
		}
	}

	class ExLoginSuccessHandler extends EzyLoginSuccessHandler {
		@Override
		protected void handleLoginSuccess(EzyData responseData) {
			client.send(new EzyAppAccessRequest(APP_NAME));
		}
	}

	class ExAccessAppHandler extends EzyAppAccessHandler {
		protected void postHandle(EzyApp app, EzyArray data) {
			sendChatMessageToOneRequest(app);
			sendGreetRequest(app);
			sendHelloRequest(app);
		}

		private void sendChatMessageToOneRequest(EzyApp app) {
			app.send("chat/sendMessageToOne", 
					EzyEntityFactory.newObjectBuilder()
						.append("to", "Dzung")
						.append("message", "Hello, I'm Dzung")
						.build());
		}
		
		private void sendGreetRequest(EzyApp app) {
			app.send("greet", 
					EzyEntityObjects.newObject("who", "Dzung"));
		}
		
		private void sendHelloRequest(EzyApp app) {
			app.send("hello", 
					EzyEntityObjects.newObject("who", "Dzung"));
		}

	}

	class ChatSendMessageResponseHandler implements EzyAppDataHandler<EzyObject> {
		public void handle(EzyApp app, EzyObject data) {
			System.out.println("Received chat data: " + data);
		}
	}
	
	class ChatGreetResponseHandler implements EzyAppDataHandler<EzyObject> {
		public void handle(EzyApp app, EzyObject data) {
			System.out.println("Received greet data: " + data);
		}
	}
	
	class ChatHelloResponseHandler implements EzyAppDataHandler<EzyObject> {
		public void handle(EzyApp app, EzyObject data) {
			System.out.println("Received hello data: " + data);
		}
	}

}
