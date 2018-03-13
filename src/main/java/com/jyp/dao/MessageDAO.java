package com.jyp.dao;

import com.jyp.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {
    String TABLE_NAME="message";
    String INSERT_FIELDS = "from_id,to_id,has_read,content,conversation_id,created_date";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values (#{fromId},#{toId},#{hasRead},#{content},#{conversationId},#{createdDate})"})
    int addMessage(Message message);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where from_id = #{userId} or to_id = #{userId} order by id desc limit #{offset},#{limit}"})
    List<Message> selectMessageByuserId(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where conversation_id = #{conversationId} order by id desc limit #{offset},#{limit}"})
    List<Message> selectMessageByConversationId(@Param("conversationId") String conversationId,@Param("offset") int offset,@Param("limit") int limit);

    @Delete({"delete",TABLE_NAME,"where id = #{id}"})
    void delById(int id);
}
