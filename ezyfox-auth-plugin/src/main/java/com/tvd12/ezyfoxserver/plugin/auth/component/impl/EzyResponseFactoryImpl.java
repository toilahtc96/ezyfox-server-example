package com.tvd12.ezyfoxserver.plugin.auth.component.impl;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;
import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.plugin.auth.command.EzyArrayResponse;
import com.tvd12.ezyfoxserver.plugin.auth.command.EzyObjectResponse;
import com.tvd12.ezyfoxserver.plugin.auth.command.impl.EzyArrayResponseImpl;
import com.tvd12.ezyfoxserver.plugin.auth.command.impl.EzyObjectResponseImpl;
import com.tvd12.ezyfoxserver.plugin.auth.component.EzyResponseFactory;

import lombok.Setter;

@Setter
@EzySingleton("responseFactory")
public class EzyResponseFactoryImpl implements EzyResponseFactory {

	@EzyAutoBind
	private EzyPluginContext pluginContext;
	
	@EzyAutoBind
	private EzyMarshaller marshaller;
	
	@Override
	public EzyArrayResponse newArrayResponse() {
		return new EzyArrayResponseImpl(pluginContext, marshaller);
	}
	
	@Override
	public EzyObjectResponse newObjectResponse() {
		return new EzyObjectResponseImpl(pluginContext, marshaller);
	}
	
}
