package com.jyp.controller;

import com.jyp.pojo.HostHolder;
import com.jyp.pojo.Message;
import com.jyp.pojo.User;
import com.jyp.pojo.ViewObject;
import com.jyp.service.MessageService;
import com.jyp.service.UserService;
import com.jyp.common.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;
    @RequestMapping(path = {"/msg/addMessage"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content)
    {
        try{
            Message message = new Message();
            message.setContent(content);
            message.setFromId(fromId);
            message.setHasRead(0);
            message.setToId(toId);
            message.setCreatedDate(new Date());
            message.setConversationId(fromId>toId ? String.format("%d_%d",fromId,toId) :String.format("%d_%d",toId,fromId));
            messageService.addMessage(message);
        }catch (Exception e)
        {
            logger.error("发送消息异常"+e.getMessage());
        }
        return ToutiaoUtil.getJSONString(0);
    }

    @RequestMapping(path= {"/msg/list"},method = {RequestMethod.GET})
    public String getUserMessage(Model model)
    {
        try{
            User user = hostHolder.getUser();
            List<Message> listMessage = messageService.getListMessageByUserId(user.getId(),0,10);
            List<ViewObject> conversations = new ArrayList<ViewObject>();
            for(Message msg : listMessage)
            {
                ViewObject vo = new ViewObject();
                vo.set("conversation",msg);
                vo.set("user",user);
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
            return "letter";
        }catch (Exception e)
        {
            logger.error("获取站内信息异常"+e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"},method = RequestMethod.GET)
    public String getDetail(@RequestParam("conversationId") String conversationId,Model model)
    {
        try{
            List<Message> listMessage = messageService.getListMessageByConversationId(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for(Message msg : listMessage)
            {
                ViewObject vo  = new ViewObject();
                User user = userService.getUser(msg.getFromId());
                vo.set("message",msg);
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);

            return "letterDetail";
        }catch (Exception e)
        {
            logger.error("获取详细消息异常"+e.getMessage());
        }
        return "letterDetail";
    }
    @RequestMapping(path = {"/msg/delcvst"},method = RequestMethod.GET)
    @ResponseBody
    public String delMsg(@RequestParam("id")int id,@RequestParam("conversationId") String conversationId,Model model)
    {
        try{
            messageService.delMessage(id);
            List<Message> listMessage = messageService.getListMessageByConversationId(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for(Message msg : listMessage)
            {
                ViewObject vo  = new ViewObject();
                User user = userService.getUser(msg.getFromId());
                vo.set("message",msg);
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);

            return "letterDetail";
        }catch (Exception e)
        {
            logger.error("获取详细消息异常"+e.getMessage());
        }
        return "letterDetail";
    }


}
