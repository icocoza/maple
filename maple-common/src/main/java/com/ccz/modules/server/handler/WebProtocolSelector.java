package com.ccz.modules.server.handler;

import com.ccz.modules.domain.constant.EProtocolType;
import com.ccz.modules.server.config.AttributeConfig;
import com.ccz.modules.server.handler.protocol.IotHeaderValues;
import com.ccz.modules.utils.KeyGen;
import com.google.common.base.Charsets;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;

@Slf4j
public class WebProtocolSelector extends ChannelInboundHandlerAdapter {

    private String websocketPath;
    private SimpleChannelInboundHandler channelInboundHandler;

    public WebProtocolSelector(String websocketPath, SimpleChannelInboundHandler channelInboundHandler) {
        this.websocketPath = websocketPath;
        this.channelInboundHandler = channelInboundHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws URISyntaxException {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            HttpHeaders headers = request.headers();

            if (ProtocolSelector.isUpgrade(headers)) {
                if(ProtocolSelector.isWebsocketProtocol(headers))
                    doServiceWebsocketProtocol(ctx, msg);
                else if(ProtocolSelector.isIoTProtocol(headers))
                    doServiceIoTProtocol(ctx, msg);
            } else {
                doServiceHttpProtocol(ctx, msg, request);
            }
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //log.error("[NettyException]" + cause.getMessage());
    }

    private static class ProtocolSelector {

        public static boolean isUpgrade(HttpHeaders headers) {
            return headers.containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE, true);
        }

        public static boolean isWebsocketProtocol(HttpHeaders headers) {
            return HttpHeaderValues.WEBSOCKET.contentEqualsIgnoreCase(headers.get(HttpHeaderNames.UPGRADE));
        }

        public static boolean isIoTProtocol(HttpHeaders headers) {
            return IotHeaderValues.IOTSOCKET.contentEqualsIgnoreCase(headers.get(HttpHeaderNames.UPGRADE));
        }

    }

    private void doServiceIoTProtocol(ChannelHandlerContext ctx, Object msg) {
        String connectionId = KeyGen.makeKey();
        ctx.channel().attr(AttributeConfig.protocolType).set(EProtocolType.IOTSOCKET);
        ctx.channel().attr(AttributeConfig.connectionId).set(connectionId);
        ChannelPipeline pipeline = ctx.pipeline();
        //TODO Put IOT Handler
        //TODO Remove HttpAggregator ref to WebSocket
        pipeline.remove("protocolSelector");
    }

    private void doServiceWebsocketProtocol(ChannelHandlerContext ctx, Object msg) {
        //log.info("Websocket Protocol Connection");
        String connectionId = KeyGen.makeKey();
        ctx.channel().attr(AttributeConfig.protocolType).set(EProtocolType.WEBSOCKET);
        ctx.channel().attr(AttributeConfig.connectionId).set(connectionId);

        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.addLast(new WebSocketServerProtocolHandler(websocketPath));
        pipeline.addLast(new WebsocketDataHandler());
        pipeline.addLast(channelInboundHandler);
        pipeline.remove("protocolSelector");
    }

    private void doServiceHttpProtocol(ChannelHandlerContext ctx, Object msg, HttpRequest request) {
        //log.info("<The Http Protocol Connection>");

        String connectionId = KeyGen.makeKey();
        FullHttpRequest req = (FullHttpRequest) msg;
        //log.info(req.headers().toString());
        //log.info(req.content().toString(Charsets.UTF_8));
        ctx.channel().attr(AttributeConfig.protocolType).set(EProtocolType.HTTP);
        ctx.channel().attr(AttributeConfig.httpHeaders).set(request.headers());
        ctx.channel().attr(AttributeConfig.connectionId).set(connectionId);

        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.addLast(new HttpOptionHandler());
        pipeline.addLast(new HttpDataHandler());
        pipeline.addLast(channelInboundHandler);
        pipeline.remove("protocolSelector");
        ctx.fireChannelRead(msg);
    }

    public class WebsocketDataHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) {
            if (msg instanceof TextWebSocketFrame) {
                ctx.fireChannelRead(msg.content().toString(Charsets.UTF_8));
            }
        }

    }

    public class HttpDataHandler extends SimpleChannelInboundHandler<Object> {

        public HttpDataHandler() {

        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
            if (msg instanceof HttpRequest) {
                FullHttpRequest req = (FullHttpRequest) msg;
                ctx.channel().attr(AttributeConfig.httpHeaders).set(req.headers());
                ctx.fireChannelRead(req.content().toString(Charsets.UTF_8));
                ReferenceCountUtil.retain(msg);
            }
        }

    }

    public class HttpOptionHandler extends SimpleChannelInboundHandler<Object> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof HttpRequest) {
                FullHttpRequest req = (FullHttpRequest) msg;
                if (req.method() == HttpMethod.OPTIONS) {
                    doOptions(ctx, req);
                    return;
                }
            }
            ctx.fireChannelRead(msg);
        }

        private void doOptions(ChannelHandlerContext ctx, FullHttpRequest req) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

            HttpHeaders headers = req.headers();
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            if (headers.contains(HttpHeaderNames.ORIGIN))
                response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, headers.get(HttpHeaderNames.ORIGIN));
            else
                response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, String.format("%s, %s, %s", HttpMethod.POST.name(), HttpMethod.GET.name(), HttpMethod.OPTIONS.name()));
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type");
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_MAX_AGE, 864000);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, true);
            ctx.writeAndFlush(response);
        }

    }

}
