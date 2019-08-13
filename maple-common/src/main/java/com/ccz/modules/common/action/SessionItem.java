package com.ccz.modules.common.action;

import com.ccz.modules.domain.inf.ISessionItem;
import io.netty.channel.Channel;

public abstract class SessionItem<T> implements ISessionItem<T> {
	protected Channel channel;
	protected int methodType;
	protected T item;
	
	public SessionItem(Channel ch, int methodType) {
		this.channel = ch;
		this.methodType = methodType;
	}
	
	public Channel getCh() {
		return channel;
	}
}
