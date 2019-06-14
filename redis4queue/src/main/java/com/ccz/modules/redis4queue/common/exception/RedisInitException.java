package com.ccz.modules.redis4queue.common.exception;

public class RedisInitException extends RuntimeException {
    private static final long serialVersionUID = 1111L;

    public RedisInitException(String s) {
        super(s);
    }

    public RedisInitException(String s, Throwable throwable) {
        super(s, throwable);
    }
}