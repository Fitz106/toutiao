package com.jyp.dao;

import com.jyp.pojo.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;


@Mapper
public interface CommentDAO {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = "content,user_id,created_date,status,entity_id,entity_type";
    String SELECT_FIELDS = "id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values (#{content},#{userId},#{createdDate},#{status},#{entityId},#{entityType})"})
    int addComment(Comment comment);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where entity_id = #{entityId} and entity_type = #{entityType}"})
    List<Comment> selectByEntityIdAndEntityType(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update",TABLE_NAME,"set status = #{status} where entity_id = #{entityId} and entity_type = #{entityType}"})
    void update(@Param("status") int status, @Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);
}
