package com.jyp.configuration;

import com.jyp.interceptor.LoginRequiredInterceptor;
import com.jyp.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
//        registry.addInterceptor(pageInterceptor);
        //registry.addInterceptor(passportInterceptor);
        super.addInterceptors(registry);
    }
}
