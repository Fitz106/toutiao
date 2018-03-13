package com.jyp.service;

import com.jyp.dao.LoginTicketDAO;
import com.jyp.dao.UserDAO;
import com.jyp.model.LoginTicket;
import com.jyp.model.User;
import com.jyp.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;

    public String addLoginTicket(int userId)
    {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*12);
        loginTicket.setExpired(date);
        loginTicketDAO.addLoginTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void updateStatus(int status,String ticket)
    {
        loginTicketDAO.updateStatusByTicket(status,ticket);
    }
    public Map<String,Object> register(String username ,String password)
    {
        Map<String ,Object> map = new HashMap<String, Object>();
        if(StringUtils.isBlank(username))
        {
            map.put("msgname","名字不能为空");
            return map;
        }
        if(StringUtils.isBlank(password))
        {
            map.put("msgpwd","密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user != null)
        {
            map.put("msgname","该名字已被注册");
            return map;
        }
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String headUrl = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(headUrl);
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        //登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }
    public Map<String,Object> login(String username ,String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "名字不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }
        if (!ToutiaoUtil.MD5(password + user.getSalt()).equals(user.getPassword()))
        {
            map.put("msgpwd","密码错误");
            return map;
        }
        map.put("userId",user.getId());
        //ticket
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }
    public void logout(String ticket)
    {
        updateStatus(1,ticket);
    }


    public User getUser(int id)
    {
        return userDAO.selectById(id);
    }
}
