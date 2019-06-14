package com.ccz.modules.redis4queue.services.redisqueue;


public interface IRedisQueueWorker {
	
	String getCommand();

	boolean doWork(String data) throws Exception;
	
	default boolean isWorkerCommand(String cmd) {
		return getCommand().equals(cmd);
	};
}
