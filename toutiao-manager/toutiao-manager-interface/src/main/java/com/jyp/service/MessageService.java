package com.jyp.service;

import com.jyp.pojo.Message;

import java.util.List;

public interface MessageService {
    public void addMessage(Message message);

    public List<Message> getListMessageByUserId(int userId, int offset, int limit);

    public List<Message> getListMessageByConversationId(String conversationId, int offset, int limit);

    public void delMessage(int id);
}
