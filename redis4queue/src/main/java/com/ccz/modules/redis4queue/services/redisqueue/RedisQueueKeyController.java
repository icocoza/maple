package com.ccz.modules.redis4queue.services.redisqueue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisQueueKeyController {
	private int threadCount;
	private Map<String, IRedisQueueWorker> redisQueueWorkerMap = new ConcurrentHashMap<>();

	private Thread[] workerThreads;
	private RedisQueueRunnable[] runnables;	//for update some worker on execution

	private IRedisQueueReader redisQueueReader;

	public RedisQueueKeyController(IRedisQueueReader redisQueueReader, int threadCount) {
		this.redisQueueReader = redisQueueReader;
		this.threadCount = threadCount;
	}

	public String getRedisKey() {
		if(redisQueueReader == null)
			return null;
		return redisQueueReader.getRedisKey();
	}
	public boolean isRedisKey(String redisKey) {
		if(redisQueueReader == null)
			return false;
		return redisQueueReader.getRedisKey() != null && redisQueueReader.getRedisKey().equals(redisKey);
	}

	public void addWorker(IRedisQueueWorker redisQueueWorker) {
		if( null == workerThreads )
			redisQueueWorkerMap.put(redisQueueWorker.getCommand(), redisQueueWorker);
		else { //on execution case
			for(int i=0; i<threadCount; i++)
				runnables[i].addWorker(redisQueueWorker);
		}
	}

	public boolean delWorker(String cmd) {
		if(redisQueueWorkerMap.containsKey(cmd) == false)
			return false;
		redisQueueWorkerMap.remove(cmd);
		for(int i=0; i<threadCount; i++) {
			runnables[i].delWorker(cmd);
		}
		return true;
	}
	
	public void startRedisQueue() {
		if( null != workerThreads )
			return;

		workerThreads = new Thread[threadCount];
		runnables = new RedisQueueRunnable[threadCount];

		for(int i=0; i<threadCount; i++) {
			runnables[i] = new RedisQueueRunnable(redisQueueReader);

			redisQueueWorkerMap.values().stream().forEach(runnables[i]::addWorker);

			workerThreads[i] = new Thread(runnables[i]);
			workerThreads[i].start();
		}
	}
	
	public void stopRedisQueue() {
		try {
			for(Thread workerThread : workerThreads) {
				workerThread.interrupt();
			}
		}catch(Exception e) {
		}
	}
	
}
