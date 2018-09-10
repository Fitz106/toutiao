package com.jyp.controller;

import com.jyp.pojo.EventModel;
import com.jyp.service.LikeService;
import com.jyp.service.LikeConsumerService;
import com.jyp.service.ProducerService;
import com.jyp.pojo.EntityType;
import com.jyp.pojo.HostHolder;

import com.jyp.service.NewsService;
import com.jyp.common.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class LikeController {
    @Autowired
    ProducerService proudcerService;

//    @Autowired
//    LikeConsumerService likeConsumerService;
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;


    @Autowired
    NewsService newsService;
    @RequestMapping(path={"/like"},method = RequestMethod.POST)
    @ResponseBody
    public String like(@Param("newsId") int newsId) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        EventModel asyncData = new EventModel()
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(newsId)
                .setEntityOwnerId(newsService.getNewsById(newsId).getUserId());
        String jsonString = ToutiaoUtil.getJsonObjectString(asyncData);
        Message msg = new Message("LikeTopic",// topic
                "TagA",// tag
                jsonString.getBytes()// body
        );
        Map<String, Object> result = proudcerService.send("LikeProducer",msg);
//        likeConsumerService.consumerLikeMsg("LikeConsumer");
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path={"/dislike"},method = RequestMethod.POST)
    @ResponseBody
    public String dislike(@Param("newsId") int newsId)
    {
        long likeCount = likeService.dislike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }

}
