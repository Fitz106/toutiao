package com.jyp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jyp.common.MailSender;
import com.jyp.common.ToutiaoUtil;
import com.jyp.service.LoginConsumerService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class LoginConsumerServiceImpl implements LoginConsumerService {
    private DefaultMQPushConsumer consumer = null;
    @Autowired
    MailSender mailSender;
    @PostConstruct
    public void init(){
        consumerLoginMsg("LoginConsumer");
    }
    public void consumerLoginMsg(String ConsumerName){

        try {
            consumer = new DefaultMQPushConsumer(ConsumerName);
            consumer.setNamesrvAddr("127.0.0.1:9876");
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.subscribe("LoginTopic", "*");
            consumer.registerMessageListener(new MessageListenerConcurrently() {

                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt > msgs,
                                                                ConsumeConcurrentlyContext context) {

                    for(int i = 0;i < msgs.size() ; i++){
                        JSONObject jsonObject = ToutiaoUtil.getJsonObject(new String(msgs.get(i).getBody()));
                        System.out.println("login consumer");
                        handle(jsonObject);
                    }
                    //返回消费状态
                    //CONSUME_SUCCESS 消费成功
                    //RECONSUME_LATER 消费失败，需要稍后重新消费
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });

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
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getString("username"));
        mailSender.sendWithHTMLTemplate(model.getString("email"), "登陆异常", "mails/welcome.html", map);
    }




}
