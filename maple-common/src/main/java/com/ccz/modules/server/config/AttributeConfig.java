package com.ccz.modules.server.config;

import com.ccz.modules.domain.constant.EProtocolType;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.AttributeKey;

public class AttributeConfig {
    public static final AttributeKey<EProtocolType> protocolType = AttributeKey.valueOf("PROTOCOL_TYPE");
    public static final AttributeKey<HttpHeaders> httpHeaders = AttributeKey.valueOf("HTTP_HEADER");
    public static final AttributeKey<String> connectionId = AttributeKey.valueOf("CONNECTION_ID");
}
