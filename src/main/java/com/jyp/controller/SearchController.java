package com.jyp.controller;

import com.jyp.model.News;
import com.jyp.model.ViewObject;
import com.jyp.service.NewsService;
import com.jyp.service.SearchService;
import com.jyp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    SearchService searchService;

//    @Autowired
//    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    @RequestMapping(path = {"/search"}, method = {RequestMethod.GET})
    public String search(Model model, @RequestParam("n") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count", defaultValue = "10") int count,
                         @RequestParam(value = "pop", defaultValue = "0") int pop) {
        try {
            List<News> newsList = searchService.searchNews(keyword, offset, count,
                    "<em>", "</em>");
            List<ViewObject> vos = new ArrayList<>();
            for (News news : newsList) {
                News n = newsService.getById(news.getId());
                ViewObject vo = new ViewObject();
                if (news.getTitle() != null) {
                    n.setTitle(news.getTitle());
                }
                vo.set("news", n);
                vo.set("user", userService.getUser(n.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyword);

            pop = 0;
            model.addAttribute("pop", pop);
        } catch (Exception e) {
            logger.error("搜索资讯失败" + e.getMessage());
        }
        return "hot";
    }
}
