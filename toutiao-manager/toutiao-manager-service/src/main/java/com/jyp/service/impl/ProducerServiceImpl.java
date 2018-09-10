package com.jyp.service.impl;

import com.jyp.service.ProducerService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service("producerService")
public class ProducerServiceImpl implements ProducerService {

    //MQ的运行地址
    public static final String MQ_IP = "127.0.0.1:9876";

    public  Map send(String produceGroupName,Message message) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {

        //1. 创建生产者连接（类似于JDBC中的Connection），要传入MQ的分组名
        DefaultMQProducer producer = new DefaultMQProducer(produceGroupName);
        //2. 设置MQ的运行地址
        producer.setNamesrvAddr(MQ_IP);
        //3. 开启连接
        producer.start();

        //4. 构造消息(重载方法较多，此处选择topic, tag, message的三参数方法)
        //5. 发送消息，该方法会返回一个发送结果的对象
        SendResult result = producer.send(message);
        System.out.println(result.getSendStatus());
        //6. 关闭连接
        producer.shutdown();

        //此处将发送结果显示在页面上，方便查看
        Map<String, Object> map = new HashMap<>();
        map.put("消息", result.getSendStatus());
        return map;
    }
}