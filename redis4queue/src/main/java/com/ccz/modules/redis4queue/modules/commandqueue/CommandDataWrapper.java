package com.ccz.modules.redis4queue.modules.commandqueue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandDataWrapper<T> {
    protected String cmd;
    protected String type;
    protected T data;

    private final Object ch;

    public CommandDataWrapper(Object ch, T data) {     //Parse cmd and content in child class
        this.ch = ch;
        this.data = data;
    }

    public CommandDataWrapper(CommandQueueData queueData) { //it has cmd, data already
        this.cmd = queueData.getCmd();
        this.type = queueData.getType();
        this.data = (T)queueData;
        this.ch = queueData.getCh();
    }

    public Object getCh() {
        return ch;
    }

    public T getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public String getCmd() {
        if(cmd != null)
            return cmd;
        return this.getCommand();
    }

    public String getCommand() {    //it could be overrided if want
        return null;
    }

}