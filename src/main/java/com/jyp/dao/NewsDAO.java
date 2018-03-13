package com.jyp.dao;

import com.jyp.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = "title,link,image,like_count,comment_count,user_id,created_date";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS, ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{userId},#{createdDate})"})
    int addNews(News news);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id = #{id}"})
    News selectById(int id);

    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Update({"update",TABLE_NAME,"set comment_count = #{commentCount} where id = #{id}"})
    void updateCommentCount(@Param("commentCount") int commentCount,@Param("id") int id);

    @Update({"update",TABLE_NAME,"set like_count = #{likeCount} where id = #{id}"})
    void updateLikeCount(@Param("likeCount") int likeCount,@Param("id") int id);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME})
    List<News> getAll();

}
