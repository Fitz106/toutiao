package com.jyp.service;


import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


public interface ProducerService {



    public  Map send(String produceGroupName,Message message) throws InterruptedException, RemotingException, MQClientException, MQBrokerException;

}