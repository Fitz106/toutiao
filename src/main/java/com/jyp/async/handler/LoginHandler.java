package com.jyp.async.handler;

import com.jyp.aspect.LogAspect;
import com.jyp.async.EventHandler;
import com.jyp.async.EventModel;
import com.jyp.async.EventType;
import com.jyp.util.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginHandler implements EventHandler{
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);
    @Autowired
    MailSender mailSender;
    @Override
    public void doHandle(EventModel model) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "登陆异常", "mails/welcome.html", map);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        List<EventType> supportEventTypes = new ArrayList<EventType>();
        supportEventTypes.add(EventType.LOGIN);
        return supportEventTypes;
    }
}
