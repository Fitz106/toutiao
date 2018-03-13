package com.jyp.service;

import com.jyp.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public long like(int userId,int entityType,int entityId)
    {
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        jedisAdapter.screm(dislikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
    public long dislike(int userId,int entityType,int entityId)
    {
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdapter.sadd(dislikeKey,String.valueOf(userId));
        jedisAdapter.screm(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
    public int getLikeStatus(int userId,int entityType,int entityId)
    {
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId)))
        {
            return 1;
        }
        //喜欢返回1，不喜欢返回-1，都不是返回0；
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        return jedisAdapter.sismember(dislikeKey,String.valueOf(userId))? -1 : 0;
    }
}
