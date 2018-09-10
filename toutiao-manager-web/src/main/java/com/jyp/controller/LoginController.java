package com.jyp.controller;


import com.jyp.pojo.EventModel;
import com.jyp.service.LoginConsumerService;
import com.jyp.service.ProducerService;
import com.jyp.service.UserService;
import com.jyp.common.ToutiaoUtil;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
   @Autowired
    ProducerService producerService;
//   @Autowired
//    LoginConsumerService loginConsumerService;

    @Autowired
    UserService userService;





    @RequestMapping(path={"/reg/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember" ,defaultValue = "0")int rememberme,
                      HttpServletResponse response)
    {
        try {
            Map<String,Object>  map = userService.register(username,password);
            if(map.containsKey("ticket"))
            {
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);

                return ToutiaoUtil.getJSONString(0,"注册成功");
            }
            else
            {
                return ToutiaoUtil.getJSONString(1,map);
            }
        }catch(Exception e){
            logger.error("注册异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"注册异常");
        }
    }

    @RequestMapping(path={"/login/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember" ,defaultValue = "0")int rememberme,
                        HttpServletResponse response)
    {
        try{
            Map<String,Object> map = userService.login(username,password);
            if(map.containsKey("ticket"))
            {
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme > 0)
                {
                    cookie.setMaxAge(3600*24*5);
                }
                model.addAttribute("user",username);
                response.addCookie(cookie);
                EventModel asyncData = new EventModel()
                        .setActorId((int)map.get("userId"))
                        .setExt("email","1197280223@qq.com")
                        .setExt("username",username);
                String jsonString = ToutiaoUtil.getJsonObjectString(asyncData);
                Message msg = new Message("LoginTopic",// topic
                        "TagA",// tag
                        jsonString.getBytes()// body
                );
                Map<String, Object> result = producerService.send("LoginProducer",msg);
//                loginConsumerService.consumerLoginMsg("LoginConsumer");
                return ToutiaoUtil.getJSONString(0,"成功登录");
            }
            else
            {
                return ToutiaoUtil.getJSONString(1,map);
            }
        }catch(Exception e)
        {
            logger.error("登录异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"登录异常");
        }
    }

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) throws MQClientException {
        userService.logout(ticket);

        return "redirect:/";
    }

}
