package com.example.simple_chat.startup;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.simple_chat.AppEntry;
import com.example.simple_chat.AppEntryLoader;
import com.example.simple_chat.plugin.PluginEntry;
import com.example.simple_chat.plugin.PluginEntryLoader;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.embedded.EzyEmbeddedServer;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyAppSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzySettingsBuilder;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzyZoneSettingBuilder;

public class SimpleChatStartup {
	
	public static void main(String[] args) throws Exception {
		
		EzyPluginSettingBuilder pluginSettingBuilder = new EzyPluginSettingBuilder()
				.name("simple-chat")
				.addListenEvent(EzyEventType.USER_LOGIN)
				.entryLoader(DecoratedPluginEntryLoader.class);
		
		EzyAppSettingBuilder appSettingBuilder = new EzyAppSettingBuilder()
				.name("simple-chat")
				.entryLoader(DecoratedAppEntryLoader.class);
		
		EzyZoneSettingBuilder zoneSettingBuilder = new EzyZoneSettingBuilder()
				.name("example")
				.application(appSettingBuilder.build())
				.plugin(pluginSettingBuilder.build());
		
		EzySimpleSettings settings = new EzySettingsBuilder()
				.zone(zoneSettingBuilder.build())
				.build();
		
		EzyEmbeddedServer server = EzyEmbeddedServer.builder()
				.settings(settings)
				.build();
		server.start();
	}
	
	public static class DecoratedPluginEntryLoader extends PluginEntryLoader {
		
		@Override
		public EzyPluginEntry load() throws Exception {
			return new PluginEntry() {
				
				protected String getPluginPath(EzyPluginSetting setting) {
					Path pluginPath = Paths.get("simple-chat-plugin");
					if(!Files.exists(pluginPath))
						pluginPath = Paths.get("../simple-chat-plugin");
					return pluginPath.toString();
				}
				
				@Override
				protected String getConfigFile(EzyPluginSetting setting) {
					return Paths.get(getPluginPath(setting), "config", "config.properties")
							.toString();
				}
			};
		}
	}
	
	public static class DecoratedAppEntryLoader extends AppEntryLoader {
		
		@Override
		public EzyAppEntry load() throws Exception {
			return new AppEntry() {
				
				protected String getAppPath() {
					Path pluginPath = Paths.get("simple-chat-entry");
					if(!Files.exists(pluginPath))
						pluginPath = Paths.get("../simple-chat-entry");
					return pluginPath.toString();
				}
				
				@Override
				protected String getConfigFile(EzyAppSetting setting) {
					return Paths.get(getAppPath(), "config", "config.properties")
							.toString();
				}
			};
		}
	}

}