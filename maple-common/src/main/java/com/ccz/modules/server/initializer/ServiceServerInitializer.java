package com.ccz.modules.server.initializer;

import com.ccz.modules.server.handler.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ServiceServerInitializer extends ChannelInitializer<SocketChannel> {

    protected IServiceActionHandler serviceActionHandler;
    protected String websocketPath;

    public ServiceServerInitializer(String websocketPath, IServiceActionHandler serviceActionHandler) {
        this.websocketPath = websocketPath;
        this.serviceActionHandler = serviceActionHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.config().setKeepAlive(true);//serverConfig.isKeepAlive());
        ch.config().setReuseAddress(true);//serverConfig.isReuseAddr());
        ch.config().setSoLinger(0);//serverConfig.getLinger());
        ch.config().setTcpNoDelay(true);//serverConfig.isTcpNoDelay());
        ChannelPipeline pipeline = ch.pipeline();

        //TODO Custom은 HttpServerCodec 앞단에서 후킹하자.
        StringBasedDataServerHandler stringBasedDataServerHandler = new StringBasedDataServerHandler(serviceActionHandler);
        pipeline.addLast("PacketDecoderSelector",
                        new PacketDecoderSelector( new WebProtocolSelector(websocketPath, stringBasedDataServerHandler),
                                stringBasedDataServerHandler,
                        new BinaryBasedDataServerHandler(serviceActionHandler) ));//iotDecoderSelector);
    }
}
