package com.example.hello_world.handler;

import static com.example.hello_world.constant.Commands.UDP_GREET;

import com.example.hello_world.common.Greeting;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzyPrototype;
import com.tvd12.ezyfox.binding.EzyDataBinding;
import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfox.core.exception.EzyBadRequestException;

import lombok.Setter;

@Setter
@EzyPrototype
@EzyObjectBinding(write = false)
@EzyRequestListener(UDP_GREET)
public class UdpGreetRequestHandler 
		extends ClientRequestHandler 
		implements EzyDataBinding {

	private String who;
	
	@EzyAutoBind
	private Greeting greeting;
	
	@Override
	protected void execute() throws EzyBadRequestException {
		responseFactory.newObjectResponse()
			.command(UDP_GREET)
			.udpTransport()
			.param("message", greeting.greet(who))
			.session(session)
			.execute();
	}
	
}
