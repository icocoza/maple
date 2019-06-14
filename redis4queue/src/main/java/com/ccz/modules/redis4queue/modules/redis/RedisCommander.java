package com.ccz.modules.redis4queue.modules.redis;

import com.ccz.modules.redis4queue.modules.redis.connection.IRedisConnection;
import redis.clients.jedis.*;
import redis.clients.jedis.params.geo.GeoRadiusParam;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RedisCommander {
    JedisCommands jCommands;

    public RedisCommander(IRedisConnection redisConnection) {
        jCommands = redisConnection.getCommands();
    }

    public String set(String key, String value) {
        String result = jCommands.set(key, value);
        ((Jedis)jCommands).close();
        return result;
    }

    public long sadd(String key, String[] setItem) {
        long result = jCommands.sadd(key, setItem);
        ((Jedis)jCommands).close();
        return result;
    }

    public long sadd(String key, String setItem) {
        long result = jCommands.sadd(key, setItem);
        ((Jedis)jCommands).close();
        return result;
    }

    public long hset(String key, String field, String value) {
        long result =  jCommands.hset(key, field, value);
        ((Jedis)jCommands).close();
        return result;
    }

    public String hmset(String key, Map<String, String> map) {
        String result =  jCommands.hmset(key, map);
        ((Jedis)jCommands).close();
        return result;
    }

    public long hincrBy(String key, String field) {
        long result =  hincrBy(key, field, 1L);
        ((Jedis)jCommands).close();
        return result;
    }

    public long hincrBy(String key, String field, long incValue) {
        long result =  jCommands.hincrBy(key, field, incValue);
        ((Jedis)jCommands).close();
        return result;
    }

    public boolean del(String key) {
        boolean result =  jCommands.del(key)>0;
        ((Jedis)jCommands).close();
        return result;

    }

    public boolean hdel(String key, String field) {
        boolean result =  jCommands.hdel(key, field)>0;
        ((Jedis)jCommands).close();
        return result;

    }

    public boolean hdel(String key, String[] fields) {
        boolean result =  jCommands.hdel(key, fields)>0;
        ((Jedis)jCommands).close();
        return result;

    }

    public long srem(String key, String[] setItem) {
        long result =  jCommands.srem(key, setItem);
        ((Jedis)jCommands).close();
        return result;

    }

    public long hlen(String key) {
        long result =  jCommands.hlen(key);
        ((Jedis)jCommands).close();
        return result;

    }

    public long decr(String key) {
        long result =  jCommands.decr(key);
        ((Jedis)jCommands).close();
        return result;

    }

    public long lpush(String key, String data) {
        long result =  jCommands.lpush(key, data);
        ((Jedis)jCommands).close();
        return result;

    }

    public long rpush(String key, String data) {
        long result =  jCommands.rpush(key, data);
        ((Jedis)jCommands).close();
        return result;

    }

    public String lpop(String key) {
        String result =  jCommands.lpop(key);
        ((Jedis)jCommands).close();
        return result;

    }

    public String rpop(String key) {
        String result =  jCommands.rpop(key);
        ((Jedis)jCommands).close();
        return result;

    }

    public String brpop(int timeout, String key) {
        List<String> values = jCommands.brpop(timeout, key);
        if(values==null || values.size()<1)
            return "";
        String result =  values.get(1);
        ((Jedis)jCommands).close();
        return result;

    }

    public String blpop(int timeout, String key) {
        List<String> values = jCommands.blpop(timeout, key);
        if(values==null || values.size()<1)
            return "";
        String result =  values.get(1);
        ((Jedis)jCommands).close();
        return result;

    }

    //slave commanders
    public String get(String key) {
        String result =  jCommands.get(key);
        ((Jedis)jCommands).close();
        return result;

    }

    public List<String> mget(String[] keys) {
        List<String> result = ((MultiKeyJedisClusterCommands)jCommands).mget(keys);
        ((Jedis)jCommands).close();
        return result;

    }

    public String hget(String key, String field) {
        String result =  jCommands.hget(key, field);
        ((Jedis)jCommands).close();
        return result;

    }

    public List<String> hmget(String key, String[] fields) {
        List<String> result = jCommands.hmget(key, fields);
        ((Jedis)jCommands).close();
        return result;

    }

    public Map<String, String> hgetAll(String key) {
        Map<String, String> result = jCommands.hgetAll(key);
        ((Jedis)jCommands).close();
        return result;

    }

    public boolean exists(String key) {
        boolean result = jCommands.exists(key);
        ((Jedis)jCommands).close();
        return result;

    }

    public boolean hexists(String key, String field) {
        boolean result = jCommands.hexists(key, field);
        ((Jedis)jCommands).close();
        return result;

    }

    public String[] smembers(String key) {
        Set<String> members = jCommands.smembers(key);
        ((Jedis)jCommands).close();
        return members.toArray(new String[members.size()]);

    }

    public boolean sismember(String key, String item) {
        boolean result = jCommands.sismember(key, item);
        ((Jedis)jCommands).close();
        return result;

    }

    public long scard(String key) {	//return count
        long result = jCommands.scard(key);
        ((Jedis)jCommands).close();
        return result;

    }

    public long addGeo(String key, double longitude, double latitude, String member) {
        long result = jCommands.geoadd(key, longitude, latitude, member);
        ((Jedis)jCommands).close();
        return result;

    }

    public List<String> getGeoRadius(String key, double longitude, double latitude, int meterRadius) {
        List<GeoRadiusResponse> list = jCommands.georadius(key, longitude, latitude, meterRadius, GeoUnit.M, GeoRadiusParam.geoRadiusParam().sortAscending());
        ((Jedis)jCommands).close();
        return list.stream().map(x -> x.getMemberByString()).collect(Collectors.toList());
    }
}