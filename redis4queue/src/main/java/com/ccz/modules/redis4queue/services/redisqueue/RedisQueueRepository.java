package com.ccz.modules.redis4queue.services.redisqueue;


import com.ccz.modules.redis4queue.modules.redis.connection.IRedisConnection;
import redis.clients.jedis.JedisCommands;

import java.util.Set;

public class RedisQueueRepository {

	private final IRedisConnection redisConnection;

	public RedisQueueRepository(IRedisConnection redisConnection) {
		this.redisConnection = redisConnection;
	}

	public boolean pushData(String redisKey, String data) {
		JedisCommands jCmd = redisConnection.getCommands();
		try {
			return jCmd.lpush(redisKey, data) > 0;
		}finally {
			redisConnection.close(jCmd);
		}
	}
	
	public String popData(String redisKey) {
		JedisCommands jCmd = redisConnection.getCommands();
		try {
			return jCmd.rpop(redisKey);
		}finally {
			redisConnection.close(jCmd);
		}
	}

	public long sadd(String key, String value) {
		JedisCommands jCmd = redisConnection.getCommands();
		try {
			return jCmd.sadd(key, value);
		}finally {
			redisConnection.close(jCmd);
		}
	}

	public String[] smembers(String key) {
		JedisCommands jCmd = redisConnection.getCommands();
		try {
			Set<String> members = jCmd.smembers(key);
			return members.toArray(new String[members.size()]);
		}finally {
			redisConnection.close(jCmd);
		}
	}

	public String get(String key) {
		JedisCommands jCmd = redisConnection.getCommands();
		try {
			return jCmd.get(key);
		}finally {
			redisConnection.close(jCmd);
		}
	}

	public void set(String key, String value) {
		JedisCommands jCmd = redisConnection.getCommands();
		try {
			jCmd.set(key, value);
		}finally {
			redisConnection.close(jCmd);
		}
	}

	public void setex(String key, String value, int exsec) {
		JedisCommands jCmd = redisConnection.getCommands();
		try {
			jCmd.setex(key, exsec, value);
		}finally {
			redisConnection.close(jCmd);
		}
	}


}
