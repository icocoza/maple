package com.ccz.modules.redis4queue.modules.redis.connection;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClusterConnection implements IRedisConnection {
	
	JedisCluster jCluster;
	
	public ClusterConnection(List<HostAndPort> hostAndPortList) {
		this(hostAndPortList, 20, 60);
	}

	public ClusterConnection(List<HostAndPort> hostAndPortList, int min, int max) {
		Set<HostAndPort> jedisClusterNodes = new HashSet<>();

		for(HostAndPort hp : hostAndPortList)
			jedisClusterNodes.add(hp);

		JedisPoolConfig config = new JedisPoolConfig();
		config.setMinIdle(min);
		config.setMaxIdle(max);

		jCluster = new JedisCluster(jedisClusterNodes, 3000, 10, config); //3000 is timeout, 10 is max redirection count
	}

	@Override
	public JedisCommands getCommands() {
		return jCluster;
	}

	@Override
	public void close(JedisCommands jedis) {
		;
	}

	@Override
	public void close() throws IOException {
		if(jCluster != null)
			jCluster.close();
	}

}
