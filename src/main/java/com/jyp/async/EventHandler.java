package com.jyp.async;

import com.jyp.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface EventHandler {
    @Autowired

    public void doHandle(EventModel model);
    public List<EventType> getSupportEventTypes();
}
