package com.jyp.service;

import com.alibaba.fastjson.JSONObject;


public interface NewsConsumerService {

    public void consumerNewsMsg(String ConsumerName);
    public void handle(JSONObject model);

}
