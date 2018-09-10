package com.jyp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jyp.common.ToutiaoUtil;
import com.jyp.service.*;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
@Component
public class NewsConsumerServiceImpl implements NewsConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(NewsConsumerServiceImpl.class);

    private DefaultMQPushConsumer consumer = null;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @Autowired
    NewsService newsService;
    @Autowired
    SearchService searchService;

    @PostConstruct
    public void init(){
        this.consumerNewsMsg("NewsConsumer");
    }
    public void consumerNewsMsg(String ConsumerName){

        try {
            consumer = new DefaultMQPushConsumer(ConsumerName);
            consumer.setNamesrvAddr("127.0.0.1:9876");
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.subscribe("NewsTopic", "*");
            consumer.registerMessageListener(new MessageListenerConcurrently() {

                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                                ConsumeConcurrentlyContext context) {

                    for(int i = 0;i < msgs.size() ; i++){
                        JSONObject jsonObject = ToutiaoUtil.getJsonObject(new String(msgs.get(i).getBody()));
                        System.out.println("news consumer");
                        handle(jsonObject);
                    }
                    //返回消费状态
                    //CONSUME_SUCCESS 消费成功
                    //RECONSUME_LATER 消费失败，需要稍后重新消费
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            //调用start()方法启动consumer

            System.out.println("Consumer Started.");
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        //休眠是为了等Spring加载完
                        Thread.sleep(5000);
                        consumer.start();

                    } catch (Exception e) {
                        //LogUtil.error(e, logger, "UserRegisterEmailConsumer启动失败");
                    }
                }
            }).start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
    public void handle(JSONObject model){
        try {
            searchService.indexNews(Integer.parseInt(model.getString("entityId")),
                    model.getString("title"));
        } catch (Exception e) {
            logger.error("增加题目索引失败");
        }
    }

}
