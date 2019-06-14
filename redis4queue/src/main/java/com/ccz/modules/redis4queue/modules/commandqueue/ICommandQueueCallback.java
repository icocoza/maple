package com.ccz.modules.redis4queue.modules.commandqueue;

public interface ICommandQueueCallback<T> {
    void onProcessQueuedCommand(Object ch, String cmd, String type, T data);
}

