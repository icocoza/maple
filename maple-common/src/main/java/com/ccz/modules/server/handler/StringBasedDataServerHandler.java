package com.ccz.modules.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringBasedDataServerHandler extends SimpleChannelInboundHandler<String> {

    IServiceActionHandler serviceActionHandler;

    public StringBasedDataServerHandler(IServiceActionHandler serviceActionHandler) {
        this.serviceActionHandler = serviceActionHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        try {
            serviceActionHandler.process(ctx.channel(), msg);
        } catch (Exception e) {
            //log.error("[ERROR_READ] TaxiChannelServerHandler::channelRead0");
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ch) throws Exception {
        super.channelInactive(ch);
        try {
            //log.info("[channelInactive] ch id: {}", ch.channel().id().asLongText());
            serviceActionHandler.onClose(ch.channel());
            ch.close();
        } catch (Exception e) {
            //log.error("[ERROR_CLOSE] TaxiChannelServerHandler::channelInactive");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //log.error("[NettyException]" + cause.getMessage());
    }

}