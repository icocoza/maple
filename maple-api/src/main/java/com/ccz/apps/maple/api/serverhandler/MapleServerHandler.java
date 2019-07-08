package com.ccz.apps.maple.api.serverhandler;

import com.ccz.modules.domain.inf.IServiceActionHandler;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapleServerHandler implements IServiceActionHandler {

    @Override
    public void start() {

    }

    @Override
    public void process(Channel ch, String msg) {
        log.info(msg);
    }

    @Override
    public void onClose(Channel ch) {

    }
}
