package com.xpp.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * 异常统一处理
 */
//拦截所有的@Controller
@ControllerAdvice
public class ControllerExceptionHandler {

    //创建获取日志，记录异常信息的对象 logger
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class) //统一处理异常
    //返回错误页面，后台输出到前端的错误信息
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {

        //调用记录器记录异常信息 public void error(String format, Object arg1, Object arg2);
        logger.error("Request URL : {}, Exception : {}", request.getRequestURL(), e);

        //如果异常类使用@ResponseStatus注解则重新抛出它，交给框架来处理
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        //创建 ModelAndView 对象，用来返回错误信息的页面
        ModelAndView mv = new ModelAndView();
        //获取URL
        mv.addObject("url", request.getRequestURL());
        //获取异常信息
        mv.addObject("exception", e);
        //返回页面的所在路径，返回到error下的error页面
        mv.setViewName("error/error");

        return mv;
    }


}
