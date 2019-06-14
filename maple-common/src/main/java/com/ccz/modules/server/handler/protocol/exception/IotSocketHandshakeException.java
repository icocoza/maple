package com.ccz.modules.server.handler.protocol.exception;

public class IotSocketHandshakeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public IotSocketHandshakeException(String s) {
        super(s);
    }

    public IotSocketHandshakeException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
