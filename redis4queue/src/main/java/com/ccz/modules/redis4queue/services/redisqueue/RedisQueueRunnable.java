package com.ccz.modules.redis4queue.services.redisqueue;

import com.ccz.modules.redis4queue.domain.redisqueue.QueueCmd;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RedisQueueRunnable implements Runnable {
	private static Logger logger = LogManager.getLogManager().getLogger(RedisQueueRunnable.class.getName());
	
	private IRedisQueueReader redisQueueReader;
	private Map<String, IRedisQueueWorker> workMap = new ConcurrentHashMap<>();

	public RedisQueueRunnable(IRedisQueueReader redisQueueReader) {
		this.redisQueueReader = redisQueueReader;
	}


	public void addWorker(IRedisQueueWorker redisQueueWorker) {
		if(null == redisQueueWorker)
			return;
		workMap.put(redisQueueWorker.getCommand(), redisQueueWorker);
	}

	public boolean delWorker(String cmd) {
		if(workMap.containsKey(cmd) == false)
			return false;
		workMap.remove(cmd);
		return true;
	}
	
	@Override
	public void run() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		while(Thread.currentThread().isInterrupted()==false) {
			try {
				String popData = null;
				if( (popData = redisQueueReader.popData()) == null) {
					Thread.sleep(1000);
					continue;
				}
				QueueCmd cmd = objectMapper.readValue(popData, QueueCmd.class);

				if(workMap.containsKey(cmd.cmd))
					workMap.get(cmd.cmd).doWork(popData);

				//[TODO] doWork 리턴값이 false 경우, 해당 command를 별도로 저장해 두어야 할것!!!
				//...
				continue;
			}catch(Exception e) {
				//logger.error(e.getMessage());
			}
			try {
				Thread.sleep(5000);	//예외 발생(cf, Redis Server Shutdown) 재시도 대기시간 5초 
			}catch(Exception e) {
			}
		}
	}

}
