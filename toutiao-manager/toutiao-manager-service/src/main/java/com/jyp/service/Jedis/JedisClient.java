package com.jyp.service.Jedis;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Set;
@Service
public class JedisClient implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisClient.class);

    private JedisPool jedisPool = null;
    private Jedis jedis = null;
    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("localhost",6379);
    }
    public String get(String key)
    {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.get(key);
        }catch (Exception e)
        {
            logger.error("jedis发生异常"+e.getMessage());
            return null;
        }finally {
            if(jedis !=null)
            {
                jedis.close();
            }
        }
    }

    public void set(String key,String value)
    {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.set(key,value);
        }catch (Exception e)
        {
            logger.error("jedis发生异常"+e.getMessage());
            }finally {
            if(jedis !=null)
            {
                jedis.close();
            }
        }
    }
    public void setex(String key,String value)
    {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.setex(key,60,value);
        }catch (Exception e)
        {
            logger.error("jedis发生异常"+e.getMessage());
        }finally {
            if(jedis !=null)
            {
                jedis.close();
            }
        }
    }

    public boolean sismember(String key,String value)
    {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e)
        {
            logger.error("jedis发生异常"+e.getMessage());
            return false;
        }finally {
            if(jedis !=null)
            {
                jedis.close();
            }
        }
    }
    public long screm(String key,String value)
    {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return  jedis.srem(key,value);
        }catch (Exception e)
        {
            logger.error("jedis发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis !=null)
            {
                jedis.close();
            }
        }
    }

    public long sadd(String key,String value)
    {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e)
        {
            logger.error("jedis发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis !=null)
            {
                jedis.close();
            }
        }
    }
    public long scard(String key)
    {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.scard(key);
        }catch (Exception e)
        {
            logger.error("jedis发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis !=null)
            {
                jedis.close();
            }
        }
    }
    public long lpush(String key,String value)
    {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.lpush(key,value);
        }catch (Exception e)
        {
            logger.error("jedis发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis !=null)
            {
                jedis.close();
            }
        }
    }
    public List<String> brpop(int timeout,String key)
    {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.brpop(timeout,key);
        }catch (Exception e)
        {
            logger.error("jedis发生异常"+e.getMessage());
            return null;
        }finally {
            if(jedis !=null)
            {
                jedis.close();
            }
        }
    }
    public void setObject(String key,Object obj)
    {
        set(key, JSON.toJSONString(obj));
    }
    public <T>  T getObject(String key,Class<T> clazz)
    {
        String value = get(key);
        if(value != null)
        {
            return JSON.parseObject(value,clazz);
        }
        return null;
    }
    public Jedis getJedis() {
        return jedisPool.getResource();
    }
    public Transaction multi(Jedis jedis) {
        try {
            return jedis.multi();
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
        }
        return null;
    }

    public List<Object> exec(Transaction tx, Jedis jedis) {
        try {
            return tx.exec();
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            tx.discard();
        } finally {
            if (tx != null) {
                try {
                    tx.close();
                } catch (IOException ioe) {
                }
            }

            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
    public Set<String> zrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
