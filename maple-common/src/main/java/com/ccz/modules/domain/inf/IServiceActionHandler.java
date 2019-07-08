package com.ccz.modules.domain.inf;

import io.netty.channel.Channel;

public interface IServiceActionHandler {
    void start();
    void onClose(Channel ch);
    void process(Channel ch, String msg);
}
