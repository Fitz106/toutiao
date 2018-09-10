package com.jyp.common;

public class RedisKeyUtil {
    private static String BIZ_EVENT = "EVENT";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE="DISLIKE";
    private static String SPLIT = ":";
    private static String BIZ_FOLLOWER = "FOLLOWER";
    private static String BIZ_FOLLOWEE = "FOLLOWEE";
    private static String BIZ_TIMELINE = "TIMELINE";
    private static String BIZ_HOTNEWS = "HOTNEWS";

    public static String getEventQueueKey()
    {
        return BIZ_EVENT;
    }
    public static String getLikeKey(int entityType,int entityId)
    {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    public static String getDislikeKey(int entityType,int entityId)
    {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    //粉丝集合
    public static String getFollowerKey(int entityType, int entityId) {
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    //关注对象集合
    public static String getFolloweeKey(int userId, int entityType) {
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    public static String getTimelineKey(int userId) {
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }
    public static String getHotNewsKey()
    {
        return BIZ_HOTNEWS;
    }
}
