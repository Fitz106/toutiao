package com.jyp.service;

import com.jyp.pojo.LoginTicket;
import com.jyp.pojo.User;

import java.util.*;

public interface UserService {
    public String addLoginTicket(int userId);


    public void updateStatus(int status, String ticket);
    public Map<String,Object> register(String username, String password);
    public Map<String,Object> login(String username, String password);
    public void logout(String ticket);
    public User selectById(int id);
    public LoginTicket selectByTicket(String ticket);

    public User getUser(int id);
}
