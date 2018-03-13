package com.jyp;

import com.jyp.dao.NewsDAO;
import com.jyp.dao.UserDAO;
import com.jyp.model.News;
import com.jyp.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@WebAppConfiguration
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;

    @Autowired
    NewsDAO newsDAO;
    @Test
    public void initData()
    {
//        Random random = new Random();
//        for(int i = 0; i < 11;i++) {
//            Date date = new Date();
//            User user = new User();
//            user.setName("USER" + i);
//            user.setPassword("");
//            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
//            user.setSalt("");
//
//            userDAO.addUser(user);
//            News news = new News();
//            news.setUserId(user.getId());
//            news.setCreatedDate(date);
//            news.setCommentCount(i+1);
//            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
//            news.setLikeCount(i+1);
//            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
//            news.setTitle(String.format("TITLE{%d}", i));
//            newsDAO.addNews(news);
//        }

    }


}
