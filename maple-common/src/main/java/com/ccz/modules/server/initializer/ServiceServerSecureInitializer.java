package com.ccz.modules.server.initializer;

import com.ccz.modules.domain.inf.IServiceActionHandler;
import com.ccz.modules.utils.SSLHelper;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

public class ServiceServerSecureInitializer extends ServiceServerInitializer {

    SSLHelper sslHelper;
    String keystorePath;
    String keystorePassword;

    public ServiceServerSecureInitializer(String websocketPath, IServiceActionHandler serviceActionHandler, String keystorePath, String keystorePassword) {
        super(websocketPath, serviceActionHandler);
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine engine = sslHelper.makeSSL(keystorePath, keystorePassword).createSSLEngine();

        engine.setUseClientMode(false);
        //engine.setNeedClientAuth(false);
        pipeline.addFirst(new SslHandler(engine));

        super.initChannel(ch);
    }

}
