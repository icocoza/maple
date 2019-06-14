package com.ccz.modules.redis4queue.services.redisqueue;

public interface IRedisQueueReader {

	void setRedisQueueRepository(RedisQueueRepository redisQueueRepository);

	String getRedisKey();
	String popData();
}
