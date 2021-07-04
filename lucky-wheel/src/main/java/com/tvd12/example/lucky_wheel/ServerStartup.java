package com.tvd12.example.lucky_wheel;

import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.embedded.EzyEmbeddedServer;
import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.setting.EzyPluginSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzySettingsBuilder;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzyZoneSettingBuilder;
import com.tvd12.ezyfoxserver.support.entry.EzyDefaultPluginEntry;

public class ServerStartup {

    private static final String ZONE_NAME = "example";
    private static final String PLUGIN_NAME = "lucky-wheel";

    public static void main(String[] args) throws Exception {
        EzyPluginSettingBuilder pluginSettingBuilder = new EzyPluginSettingBuilder()
            .name(PLUGIN_NAME)
            .addListenEvent(EzyEventType.USER_LOGIN)
            .entryLoader(HelloPluginEntryLoader.class);

        EzyZoneSettingBuilder zoneSettingBuilder = new EzyZoneSettingBuilder()
            .name(ZONE_NAME)
            .plugin(pluginSettingBuilder.build());

        EzySimpleSettings settings = new EzySettingsBuilder()
            .zone(zoneSettingBuilder.build())
            .build();

        EzyEmbeddedServer server = EzyEmbeddedServer.builder()
            .settings(settings)
            .build();
        server.start();
    }

    public static class HelloPluginEntryLoader extends EzyAbstractPluginEntryLoader {
        @Override
        public EzyPluginEntry load() throws Exception {
            return new HelloPluginEntry();
        }
    }

    public static class HelloPluginEntry extends EzyDefaultPluginEntry {

        @Override
        protected String[] getScanableBeanPackages() {
            return new String[] {
                "com.tvd12.example.lucky_wheel"
            };
        }

    }
}
