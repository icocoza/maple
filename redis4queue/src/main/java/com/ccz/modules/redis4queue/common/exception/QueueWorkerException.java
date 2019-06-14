package com.ccz.modules.redis4queue.common.exception;

public class QueueWorkerException extends RuntimeException {
    private static final long serialVersionUID = 1112L;

    public QueueWorkerException(String s) {
        super(s);
    }

    public QueueWorkerException(String s, Throwable throwable) {
        super(s, throwable);
    }
}

