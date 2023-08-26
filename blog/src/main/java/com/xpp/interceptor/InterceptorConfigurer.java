package com.xpp.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 */
@Configuration
public class InterceptorConfigurer implements WebMvcConfigurer {
    /*
    注册一个自己开发的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //初始化拦截器对象
        //HandlerInterceptor loginInterceptor = new LoginInterceptor();
        //注册拦截器
        //InterceptorRegistration ir = registry.addInterceptor(loginInterceptor);
        //设置黑/白名单
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/login");
    }
}
