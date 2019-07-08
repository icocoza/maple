package com.ccz.modules.domain.inf;


import io.netty.channel.Channel;

import java.util.Map;

public interface ICommandProcess<T> {
	Map<T, ICommandFunction> getCommandFunctions();
	void send(Channel ch, String data) ;
}
