package com.ccz.modules.redis4queue.modules.commandqueue;

public class CommandQueueData<T> {
    private final Object ch;
    private final String cmd;
    private final String type;
    private final T data;

    public CommandQueueData(Object ch, String cmd, String type, T data) {
        this.ch  = ch;
        this.cmd = cmd;
        this.type = type;
        this.data = data;
    }

    public CommandQueueData(String cmd, String type, T data) {
        this(null, cmd, type, data);
    }

    public Object getCh() {    return ch;  }
    public String getCmd() {    return cmd; }
    public String getType() {    return type; }
    public T getData() {   return data;    }

}