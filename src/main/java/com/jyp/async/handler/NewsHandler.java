package com.jyp.async.handler;

import com.jyp.async.EventHandler;
import com.jyp.async.EventModel;
import com.jyp.async.EventType;
import com.jyp.model.Message;
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
public class NewsHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(NewsHandler.class);
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
        try {
            searchService.indexNews(model.getEntityId(),
                    model.getExt("title"));
        } catch (Exception e) {
            logger.error("增加题目索引失败");
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.NEWS);
    }
}
