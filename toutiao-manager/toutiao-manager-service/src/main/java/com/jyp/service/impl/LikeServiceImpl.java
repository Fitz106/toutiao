package com.jyp.service.impl;


import com.jyp.common.RedisKeyUtil;
import com.jyp.service.Jedis.JedisClient;
import com.jyp.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("likeService")
public class LikeServiceImpl implements LikeService {
    @Autowired
    JedisClient jedisService;

    public long like(int userId,int entityType,int entityId)
    {
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisService.sadd(likeKey,String.valueOf(userId));
        jedisService.screm(dislikeKey,String.valueOf(userId));
        return jedisService.scard(likeKey);
    }
    public long dislike(int userId,int entityType,int entityId)
    {
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisService.sadd(dislikeKey,String.valueOf(userId));
        jedisService.screm(likeKey,String.valueOf(userId));
        return jedisService.scard(likeKey);
    }
    public int getLikeStatus(int userId,int entityType,int entityId)
    {
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        if(jedisService.sismember(likeKey,String.valueOf(userId)))
        {
            return 1;
        }
        //喜欢返回1，不喜欢返回-1，都不是返回0；
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        return jedisService.sismember(dislikeKey,String.valueOf(userId))? -1 : 0;
    }
}
