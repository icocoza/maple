package com.ccz.modules.server.handler;

import io.netty.channel.Channel;

public interface IServiceActionHandler {
    void start();
    void onClose(Channel ch);
    void process(Channel ch, String msg);
}
