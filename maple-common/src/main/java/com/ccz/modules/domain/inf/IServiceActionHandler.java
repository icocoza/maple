package com.ccz.modules.domain.inf;

import com.ccz.modules.controller.common.CommonForm;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;

public interface IServiceActionHandler {
    void start();
    void onClose(Channel ch);
    String process(Channel ch, String msg);
    String process(Channel ch, JsonNode jNode);
    String process(Channel ch, CommonForm form);
}
