package com.jyp.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jyp.service.JedisAdapter;
import com.jyp.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);
    @Autowired
    JedisAdapter jedisAdapter;
    public boolean fireEvent(EventModel model)
    {
        try {
            String value = JSONObject.toJSONString(model);
            String eventQueueKey = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(eventQueueKey, value);
            return true;
        }catch (Exception e)
        {
            logger.error("发送事件异常"+e.getMessage());
            return false;
        }
    }
}
