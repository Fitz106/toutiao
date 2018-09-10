package com.jyp.service;

import com.jyp.pojo.News;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface NewsService {
    public int addNews(News news);



    public void updateCommentCount(int id, int commentCount);


    public List<News> getLatestNewsByUserId(int userId, int offset, int limit);

    public void addHotNews(News news);

    public List<News> getHotNews(int count);

    public News getNewsById(int id);


    public void updateLikeCount(int id, int likeCount);


    public double getNewsScore(News news);

    public void setNewsScore(News news);
    public List<Integer> getIdsFromSet(Set<String> idset) ;
    public News getById(int id);

    public List<News> getAll();
}
