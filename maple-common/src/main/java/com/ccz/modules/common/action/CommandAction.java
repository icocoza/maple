package com.ccz.modules.common.action;

import com.ccz.modules.domain.inf.ICommandFunction;
import com.ccz.modules.domain.inf.IStringCommandProcess;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CommandAction implements IStringCommandProcess {

	private Map<String, ICommandFunction> cmdFuncMap = new ConcurrentHashMap<>();

	public void send(Channel ch, String data) {
		ProtocolWriter.IWriteProtocol wp = ch.attr(ChAttributeKey.getWriteKey()).get();
		wp.write(ch, data);
	}

	@Override
	public Map<String, ICommandFunction> getCommandFunctions() {
		return cmdFuncMap;
	}

	public void setCommandFunction(String cmd, ICommandFunction func) {
		cmdFuncMap.put(cmd, func);
	}
}