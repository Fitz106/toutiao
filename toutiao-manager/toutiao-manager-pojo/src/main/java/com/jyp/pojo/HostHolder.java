package com.jyp.pojo;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class HostHolder implements Serializable {
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
