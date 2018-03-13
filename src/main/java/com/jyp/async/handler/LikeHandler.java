package com.jyp.async.handler;

import com.jyp.async.EventHandler;
import com.jyp.async.EventModel;
import com.jyp.async.EventType;
import com.jyp.model.Message;
import com.jyp.model.News;
import com.jyp.model.User;
import com.jyp.service.MessageService;
import com.jyp.service.NewsService;
import com.jyp.service.SearchService;
import com.jyp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class LikeHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(LikeHandler.class);
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @Autowired
    NewsService newsService;
    @Autowired
    SearchService searchService;
    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setFromId(1);
        message.setToId(model.getEntityOwnerId());
        User user = userService.getUser(model.getActorId());
        News news = newsService.getNewsById(model.getEntityId());
        message.setContent(user.getName() + "给你的资讯" + "《" + news.getTitle() + "》" + "点了赞" );
        message.setConversationId(String.format("%d_%d",1,model.getEntityOwnerId()));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
