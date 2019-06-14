package com.ccz.modules.redis4queue.modules.redis.connection;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

public class SingleConnection implements IRedisConnection {
	
	JedisPool jPool = null;
	
	public SingleConnection(String masters) {	//master = ip:port,ip:port
		JedisPoolConfig jedisConfig = new JedisPoolConfig();
		jedisConfig.setMinIdle(8);
		jedisConfig.setMaxIdle(16);
		jedisConfig.setMaxTotal(128);
		jPool = new JedisPool(jedisConfig, masters);
	}
	
	public JedisCommands getCommands() {
		return jPool.getResource();
	}

	@Override
	public void close(JedisCommands jedis) {
		try{
			((Jedis)jedis).close();
		}catch (Exception e) {
			;
		}
	}

	@Override
	public void close() throws IOException {
		if(jPool != null)
			jPool.close();
	}

}
