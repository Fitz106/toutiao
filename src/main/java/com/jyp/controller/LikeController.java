package com.jyp.controller;

import com.jyp.async.EventModel;
import com.jyp.async.EventProducer;
import com.jyp.async.EventType;
import com.jyp.model.EntityType;
import com.jyp.model.HostHolder;
import com.jyp.service.LikeService;
import com.jyp.service.NewsService;
import com.jyp.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {


    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;
    @Autowired
    NewsService newsService;
    @RequestMapping(path={"/like"},method = RequestMethod.POST)
    @ResponseBody
    public String like(@Param("newsId") int newsId)
    {
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(newsId)
                .setEntityOwnerId(newsService.getNewsById(newsId).getUserId()));
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
