package com.jyp.service;

import com.jyp.pojo.News;


import java.util.List;


public interface SearchService {
    public List<News> searchNews(String keyword, int offset, int count,
                                 String hlPre, String hlPos) throws Exception ;


    public boolean indexNews(int qid, String title) throws Exception ;

}
