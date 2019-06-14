package com.ccz.modules.redis4queue.common.exception;

public class RedisKeyException  extends RuntimeException {
    private static final long serialVersionUID = 1113L;

    public RedisKeyException(String s) {
        super(s);
    }

    public RedisKeyException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
