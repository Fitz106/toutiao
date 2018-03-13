package com.jyp.controller;


import com.jyp.model.EntityType;
import com.jyp.model.HostHolder;
import com.jyp.model.News;
import com.jyp.model.ViewObject;
import com.jyp.service.LikeService;
import com.jyp.service.NewsService;
import com.jyp.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;

    private List<ViewObject> getLastNews(int userId, int offset, int limit) {
        //userId 为0时，则以news的Id的降序筛选news
        List<News> newsList = newsService.getLatestNewsByUserId(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        System.out.println(localUserId);
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));
            if(localUserId >0)
            {
                int like = likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId());
                vo.set("like",like);
            }
            else
            {
                vo.set("like",0);
            }
            vos.add(vo);
        }
        return vos;
    }
    private List<ViewObject> getHotNews(int count)
    {
        List<News> newsList = newsService.getHotNews(count);
        List<ViewObject> vos = new ArrayList<>();
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        System.out.println(localUserId);
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));
            if(localUserId >0)
            {
                int like = likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId());
                vo.set("like",like);
            }
            else
            {
                vo.set("like",0);
            }
            vos.add(vo);
        }
        return vos;
    }
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,@RequestParam(value = "pop", defaultValue = "0") int pop) {
        List<ViewObject>  vos = getLastNews(0,0,50);
        model.addAttribute("vos",vos);
        if (hostHolder.getUser() != null) {
            pop = 0;
        }
        model.addAttribute("pop", pop);
        return "home";
    }
    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId)
    {
        model.addAttribute("vos",getLastNews(userId,0,50));
        return "home";
    }
    @RequestMapping(path = {"/hot"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String hotIndex(Model model,@RequestParam(value = "pop", defaultValue = "0") int pop) {
        List<ViewObject>  vos = getHotNews(20);
        model.addAttribute("vos",vos);
        if (hostHolder.getUser() != null) {
            pop = 0;
        }
        model.addAttribute("pop", pop);
        return "hot";
    }
}
