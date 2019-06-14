package com.ccz.modules.redis4queue.services;

import com.ccz.modules.redis4queue.common.exception.QueueWorkerException;
import com.ccz.modules.redis4queue.common.exception.RedisInitException;
import com.ccz.modules.redis4queue.common.exception.RedisKeyException;
import com.ccz.modules.redis4queue.modules.redis.RedisCommander;
import com.ccz.modules.redis4queue.modules.redis.connection.ClusterConnection;
import com.ccz.modules.redis4queue.modules.redis.connection.IRedisConnection;
import com.ccz.modules.redis4queue.modules.redis.connection.SentinelConnection;
import com.ccz.modules.redis4queue.modules.redis.connection.SingleConnection;
import com.ccz.modules.redis4queue.services.redisqueue.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.HostAndPort;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RedisCommandManager {
	private static RedisCommandManager s_pThis;

	public static RedisCommandManager getInst() {
		if(s_pThis==null)
			s_pThis = new RedisCommandManager();
		return s_pThis;
	}

	public static void freeInst() {
		s_pThis = null;
	}

	private int threadCountForEachController = 5;

	private boolean redisOnClusterMode = false;

	private Map<String, RedisQueueKeyController> redisQueueKeyControllerMap = new ConcurrentHashMap<>();

	private IRedisConnection redisConnection;
	private RedisQueueRepository redisQueueRepository = null;
	private RedisQueueWriter redisQueueWriter = null;

	public void initRedisCluster(List<HostAndPort> hostAndPort) {
		this.redisOnClusterMode = true;
		redisConnection = new ClusterConnection(hostAndPort);
		initQueueRepository(redisConnection);
	}

	public void initRedisSentinel(String name, List<String> hostAndPort, String passwd) {
		this.redisOnClusterMode = false;
		redisConnection = new SentinelConnection(name, hostAndPort, passwd);
		initQueueRepository(redisConnection);
	}

	public void initRedisSingle(String host) {
		redisConnection = new SingleConnection(host);
		initQueueRepository(redisConnection);
	}

	private void initQueueRepository(IRedisConnection redisConnection) {
		redisQueueRepository = new RedisQueueRepository(redisConnection);
		redisQueueWriter = new RedisQueueWriter();
		redisQueueWriter.setRedisQueueRepository(redisQueueRepository);
	}

	public void addControllerByDefaultValue(String redisKey) {
		if(redisQueueKeyControllerMap.containsKey(redisKey))
			throw new RedisKeyException("Already Exist RedisKey");
		if(redisConnection == null)
			throw new RedisInitException("Init Redis Cluster or Sentinel in advance");

		RedisQueueReader redisQueueReader = new RedisQueueReader(redisKey);
		redisQueueReader.setRedisQueueRepository(this.redisQueueRepository);
		RedisQueueKeyController redisQueueKeyController = new RedisQueueKeyController(redisQueueReader, threadCountForEachController);
		redisQueueKeyControllerMap.put(redisKey, redisQueueKeyController);
	}

	public void addControllerWithQueueReader(IRedisQueueReader redisQueueReader) {
		if(redisQueueReader.getRedisKey() == null)
			throw new RedisKeyException("No Key in RedisQueueReader");
		if(redisQueueKeyControllerMap.containsKey(redisQueueReader.getRedisKey()))
			throw new RedisKeyException("Already Exist RedisKey");
		if(redisConnection == null)
			throw new RedisInitException("Init Redis Cluster or Sentinel in advance");

		redisQueueReader.setRedisQueueRepository(this.redisQueueRepository);
		RedisQueueKeyController redisQueueKeyController = new RedisQueueKeyController(redisQueueReader, threadCountForEachController);
		redisQueueKeyControllerMap.put(redisQueueReader.getRedisKey(), redisQueueKeyController);
	}

	public void delController(String redisKey) {
		if(redisQueueKeyControllerMap.containsKey(redisKey))
			throw new RedisKeyException("Already Exist RedisKey");

		RedisQueueKeyController redisQueueKeyController = redisQueueKeyControllerMap.remove(redisKey);
		redisQueueKeyController.stopRedisQueue();
	}

	public void addQueueWorker(String redisKey, IRedisQueueWorker redisQueueWorker) {
		if(redisQueueKeyControllerMap.containsKey(redisKey) == false)
			throw new RedisKeyException("Not Exist RedisKey");
		if(redisQueueWorker.getCommand() == null)
			throw new QueueWorkerException("Not Exist Queue Command");

		redisQueueKeyControllerMap.get(redisKey).addWorker(redisQueueWorker);
	}

	public boolean delQueueWorker(String redisKey, String cmd) {
		if(redisQueueKeyControllerMap.containsKey(redisKey) == false)
			throw new RedisKeyException("Not Exist RedisKey");
		return redisQueueKeyControllerMap.get(redisKey).delWorker(cmd);
	}
	
	public void startRedisQueue() {
		redisQueueKeyControllerMap.values().stream().forEach(x -> x.startRedisQueue());
	}
	
	public void stopRedisQueue() throws IOException {
		redisQueueKeyControllerMap.values().stream().forEach(x -> x.stopRedisQueue());
		redisConnection.close();
	}

	public void setThreadCountForEachController(int threadCountForEachController) {
		this.threadCountForEachController = threadCountForEachController;
	}

	public void setRedisOnClusterMode(boolean redisOnClusterMode) {
		this.redisOnClusterMode = redisOnClusterMode;
	}

	public boolean writeQueueData(String redisKey, String data) {
		if(redisConnection == null)
			throw new RedisInitException("Init Redis Cluster or Sentinel in advance");
		return new RedisCommander(this.redisConnection).lpush(redisKey, data) > 0;
	}

	public <T>boolean writeQueueData(String redisKey, T data) throws JsonProcessingException {
		if(redisConnection == null)
			throw new RedisInitException("Init Redis Cluster or Sentinel in advance");
		String json = new ObjectMapper().writeValueAsString(data);
		return new RedisCommander(this.redisConnection).lpush(redisKey, json) > 0;
	}

	public void addQueueDataForTest(String key, String value) {
		new RedisCommander(this.redisConnection).set("cocoza", "test");
		new RedisCommander(this.redisConnection).lpush(key, value);
	}

	public long sadd(String key, String value) {
		return redisQueueRepository.sadd(key, value);
	}

	public String[] smembers(String key) {
		return redisQueueRepository.smembers(key);
	}

	public String get(String key) {
		return redisQueueRepository.get(key);
	}
	public void set(String key, String value) {
		redisQueueRepository.set(key, value);
	}

	public void setex(String key, String value, int exsec) {
		redisQueueRepository.setex(key, value, exsec);
	}


}
