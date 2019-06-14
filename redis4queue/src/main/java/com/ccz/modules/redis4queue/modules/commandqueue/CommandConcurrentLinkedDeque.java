package com.ccz.modules.redis4queue.modules.commandqueue;

import java.util.concurrent.ConcurrentLinkedDeque;

public class CommandConcurrentLinkedDeque extends ConcurrentLinkedDeque<CommandDataWrapper> {

    int count;
    @Override
    public CommandDataWrapper poll() {
        CommandDataWrapper commandWrapper = super.poll();
        incCount(-1);
        return commandWrapper;
    }

    @Override
    public boolean add(CommandDataWrapper cmd) {
        boolean isAdded = super.add(cmd);
        incCount(1);
        return isAdded;
    }

    @Override
    public int size() {
        return count;
    }

    private synchronized  void incCount(int value) {
        count += value;
    }
}

