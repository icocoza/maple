package com.ccz.modules.redis4queue.services.redisqueue;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RedisQueueWriter {
	private static Logger logger = LogManager.getLogManager().getLogger(RedisQueueWriter.class.getName());

	private RedisQueueRepository redisQueueRepository;

	public RedisQueueWriter() {
		;
	}

	public void setRedisQueueRepository(RedisQueueRepository redisQueueRepository) {
		this.redisQueueRepository = redisQueueRepository;
	}

	public boolean write(String redisKey, String data) {
		return this.redisQueueRepository.pushData(redisKey, data);
	}

}
