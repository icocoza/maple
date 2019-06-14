package com.ccz.modules.redis4queue.modules.redis.connection;

import redis.clients.jedis.JedisCommands;

import java.io.IOException;

public interface IRedisConnection {
	JedisCommands getCommands();
	void close(JedisCommands jedis);
	void close() throws IOException;
}
