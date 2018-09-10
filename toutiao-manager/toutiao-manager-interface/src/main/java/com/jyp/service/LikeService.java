package com.jyp.service;

public interface LikeService {
    public long like(int userId, int entityType, int entityId);
    public long dislike(int userId, int entityType, int entityId);
    public int getLikeStatus(int userId, int entityType, int entityId);

}
