package com.ccz.modules.domain.constant;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public enum EProtocolHeader {
    iotp1("IOTP/1.1"),
    iotp2("IOTP/1.2"),
    http("http"),
    comma_delimiter("DELIMITER/COMMA"),
    none("none");

    public final String value;

    EProtocolHeader(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static final Map<String, EProtocolHeader> protocolMap;

    static {
        protocolMap = new ConcurrentHashMap<>();
        Arrays.stream(EProtocolHeader.values()).forEach(cmd -> protocolMap.put(cmd.getValue(), cmd));
    }

    public static EProtocolHeader getType(String cmd) {
        //log.info(cmd);
        EProtocolHeader ecmd = cmd == null ? null : protocolMap.get(cmd);

        if(ecmd == null) {
            cmd = cmd.toUpperCase();
            if(cmd.startsWith("GET ") || cmd.startsWith("POST ") || cmd.startsWith("PUT ") || cmd.startsWith("DELETE "))
                return http;
            return none;
        }
        return ecmd;
    }

}
