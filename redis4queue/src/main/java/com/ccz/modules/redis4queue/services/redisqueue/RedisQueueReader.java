package com.ccz.modules.redis4queue.services.redisqueue;

import com.ccz.modules.redis4queue.domain.redisqueue.QueueCmd;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RedisQueueReader implements IRedisQueueReader{
	private static Logger logger = LogManager.getLogManager().getLogger(RedisQueueReader.class.getName());
	
	private String redisKey;
	private RedisQueueRepository redisQueueRepository;
	private ObjectMapper objectMapper = new ObjectMapper();
	private QueueCmd queueCmd;
	private String popData;

	public RedisQueueReader(String redisKey) {
		this.redisKey = redisKey;
	}

	@Override
	public void setRedisQueueRepository(RedisQueueRepository redisQueueRepository) {
		this.redisQueueRepository = redisQueueRepository;
	}

	@Override
	public String getRedisKey() {
		return this.redisKey;
	}

	@Override
	public String popData() {
		return this.redisQueueRepository.popData(redisKey);
	}

}
