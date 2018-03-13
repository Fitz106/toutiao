package com.jyp.interceptor;

import com.jyp.dao.LoginTicketDAO;
import com.jyp.dao.UserDAO;
import com.jyp.model.HostHolder;
import com.jyp.model.LoginTicket;
import com.jyp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LoginTicketDAO loginTicketDAO;
    @Autowired
    UserDAO userDAO;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = null;
        if(request.getCookies() != null)
        {
            for(Cookie cookie : request.getCookies())
            {
                if(cookie.getName().equals("ticket"))
                {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if(ticket != null)
        {
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            //防止伪造ticket，ticket是否失效，是否已登出
            if(loginTicket == null ||  loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() !=0 )
            {
                return true;
            }
            else
            {
                User user = userDAO.selectById(loginTicket.getUserId());
                hostHolder.setUser(user);
            }
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null)
        {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
