package com.ccz.modules.redis4queue.modules.redis.connection;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisSentinelPool;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

public class SentinelConnection implements IRedisConnection {

	JedisSentinelPool sentinelPool = null;

	public SentinelConnection(String masterName, List<String> hostAndPort, String passwd) {
		this(masterName, hostAndPort, passwd, 20, 60);
	}

	public SentinelConnection(String masterName, List<String> hostAndPort, String passwd, int min, int max) {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMinIdle(min);
		config.setMaxTotal(max);
		sentinelPool = new JedisSentinelPool(masterName, new HashSet<String>(hostAndPort), passwd);
	}
	
	public JedisCommands getCommands() {
		return sentinelPool.getResource();
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
		if(sentinelPool != null)
			sentinelPool.close();
	}


}
