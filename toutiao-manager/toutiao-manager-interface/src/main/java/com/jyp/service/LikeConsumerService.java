package com.jyp.service;

import com.alibaba.fastjson.JSONObject;


import org.springframework.stereotype.Service;


@Service
public interface LikeConsumerService {

    public  void handle(JSONObject model);


    public  void consumerLikeMsg(String ConsumerName) ;

}

