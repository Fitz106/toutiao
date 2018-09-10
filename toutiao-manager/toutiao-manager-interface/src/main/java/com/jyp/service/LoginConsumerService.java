package com.jyp.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.exception.MQClientException;


public interface LoginConsumerService{

    public void consumerLoginMsg(String ConsumerName) throws MQClientException;
    public void handle(JSONObject model);
}
