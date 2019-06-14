package com.ccz.modules.redis4queue.modules.commandqueue;

import java.util.Arrays;

public class CommandQueueManager {

    private static CommandQueueManager s_pThis;

    public static CommandQueueManager getInst() {
        if(s_pThis==null)
            s_pThis = new CommandQueueManager();
        return s_pThis;
    }

    public static void freeInst() {
        if(s_pThis!=null)
            s_pThis.stop();
        s_pThis = null;
    }

    private CommandConcurrentLinkedDeque commandDeque = new CommandConcurrentLinkedDeque();

    private ICommandQueueCallback commandQueueCallback;
    private CommandQueueRunnable[] commandQueueRunnables;

    private int threadCount;
    private int waitMillisecond;

    public void setCommandQueueCallback(ICommandQueueCallback commandQueueCallback) {
        this.commandQueueCallback = commandQueueCallback;
    }

    public void setCommandQueueConfig(int threadCount, int waitMillisecond) {
        this.threadCount = threadCount;
        this.waitMillisecond = waitMillisecond;
    }

    public void add(CommandDataWrapper cmd) {
        commandDeque.add(cmd);
    }

    public void start() {
        if (null != commandQueueRunnables) {
            return;
        }
        commandQueueRunnables = new CommandQueueRunnable[threadCount];
        for (int i = 0; i < commandQueueRunnables.length; i++) {
            commandQueueRunnables[i] = new CommandQueueRunnable(commandDeque, commandQueueCallback, waitMillisecond);
            new Thread(commandQueueRunnables[i]).start();
        }
    }

    public void stop() {
        if (null != commandQueueRunnables)
            Arrays.stream(commandQueueRunnables).forEach(CommandQueueRunnable::stopThread);
    }

    public int getCount() {
        return commandDeque.size();
    }


}
