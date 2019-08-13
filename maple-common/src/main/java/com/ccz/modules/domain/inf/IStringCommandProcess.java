package com.ccz.modules.domain.inf;


import com.ccz.modules.common.repository.CommonRepository;
import io.netty.channel.Channel;

import java.util.Map;

public interface IStringCommandProcess {

	void initCommandFunctions(CommonRepository commonRepository);
	String makeCommandId(Enum e);

	Map<String, ICommandFunction> getCommandFunctions();

	void send(Channel ch, String data);
}
