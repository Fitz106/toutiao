package com.jyp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jyp.common.ToutiaoUtil;
import com.jyp.pojo.Message;
import com.jyp.pojo.News;
import com.jyp.pojo.User;
import com.jyp.service.*;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Component
public class LikeConsumerServiceImpl implements LikeConsumerService {
    private static DefaultMQPushConsumer consumer = null;
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
        this.consumerLikeMsg("LikeConsumer");
    }
    public  void handle(JSONObject model){
        Message message = new Message();
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setFromId(1);
        message.setToId(Integer.parseInt(model.getString("entityOwnerId")));
        User user = userService.getUser(Integer.parseInt(model.getString("actorId")));
        News news = newsService.getNewsById(Integer.parseInt(model.getString("entityId")));
        message.setContent(user.getName() + "给你的资讯" + "《" + news.getTitle() + "》" + "点了赞" );
        message.setConversationId(String.format("%d_%d",1,Integer.parseInt(model.getString("entityOwnerId"))));
        messageService.addMessage(message);
    }

    public  void consumerLikeMsg(String ConsumerName){

        try {
            consumer = new DefaultMQPushConsumer(ConsumerName);
            consumer.setNamesrvAddr("127.0.0.1:9876");
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.subscribe("LikeTopic", "*");
            consumer.registerMessageListener(new MessageListenerConcurrently() {

                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt > msgs,
                                                                ConsumeConcurrentlyContext context) {

                    for(int i = 0;i < msgs.size() ; i++){
                        System.out.println("consumer");
                        JSONObject jsonObject = ToutiaoUtil.getJsonObject(new String(msgs.get(i).getBody()));
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

}

