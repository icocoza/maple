package com.ccz.modules.redis4queue.modules.commandqueue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandQueueRunnable implements Runnable {
    private final CommandConcurrentLinkedDeque taxiCommandDeque;
    private final ICommandQueueCallback commandQueueCallback;
    private final int waitMillisecond;
    private boolean stoppedThread = false;

    public CommandQueueRunnable(CommandConcurrentLinkedDeque taxiCommandDeque, ICommandQueueCallback commandQueueCallback, int waitMillisecond) {
        this.taxiCommandDeque = taxiCommandDeque;
        this.commandQueueCallback = commandQueueCallback;
        this.waitMillisecond = waitMillisecond;
    }

    public void stopThread() {
        stoppedThread = true;
    }

    @Override
    public void run() {
        while (!stoppedThread && !Thread.currentThread().isInterrupted()) {
            try {
                CommandDataWrapper cmdWrapper = taxiCommandDeque.poll();
                if (cmdWrapper == null) {
                    Thread.sleep(waitMillisecond);    //wait milliseconds if no data in queuehandler
                    continue;
                }

                commandQueueCallback.onProcessQueuedCommand(cmdWrapper.getCh(), cmdWrapper.getCmd(), cmdWrapper.getType(), cmdWrapper.getData());
            } catch (Exception e) {
                //log.error("[Exception]" + e.getMessage());
            }
        }
        //log.info("##CommandQueue thread is stopped.");
    }

}

