package com.ccz.modules.server.handler.protocol;

import com.ccz.modules.server.handler.protocol.exception.IotSocketHandshakeException;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class IotSocketServerHandshaker {

    public FullHttpResponse newHandshake(FullHttpRequest req, HttpHeaders headers) {
        if (!req.headers().containsValue(IotHeaderNames.CONNECTION, IotHeaderValues.UPGRADE, true)
                || !IotHeaderValues.IOTSOCKET.contentEqualsIgnoreCase(req.headers().get(IotHeaderNames.UPGRADE))) {
            throw new IotSocketHandshakeException("not a IotSocket handshake request: missing upgrade");
        }
        FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, new HttpResponseStatus(200,
                "IoT Socket Protocol Handshake"));

        res.headers().add(IotHeaderNames.UPGRADE, IotHeaderValues.IOTSOCKET);
        res.headers().add(IotHeaderNames.CONNECTION, IotHeaderValues.UPGRADE);
        return res;
    }
}
