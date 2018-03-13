package com.jyp.service;

import com.jyp.dao.MessageDAO;
import com.jyp.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    public void addMessage(Message message)
    {
        messageDAO.addMessage(message);
    }

    public List<Message> getListMessageByUserId(int userId,int offset,int limit)
    {
        return messageDAO.selectMessageByuserId(userId,offset,limit);
    }
    public List<Message> getListMessageByConversationId(String conversationId,int offset,int limit)
    {
        return messageDAO.selectMessageByConversationId(conversationId,offset,limit);
    }
    public void delMessage(int id){
        messageDAO.delById(id);
    }
}
