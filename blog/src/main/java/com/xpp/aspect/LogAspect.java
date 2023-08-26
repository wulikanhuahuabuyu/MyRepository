package com.xpp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 日志处理
 */
@Aspect  //定义一个切面
@Component //组件扫描
public class LogAspect {

    //创建一个记录器
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //@Pointcut:声明一个切入点
    //execution(*  .*.*(..))：规定被拦截类（所在路径）
    @Pointcut("execution(* com.xpp.web.*.*(..))")
    public void log() {

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        //声明 ServletRequestAttributes 对象来获取HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        //获取HttpServletRequest
        HttpServletRequest request = attributes.getRequest();

        //获取传递过来的参数
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        //获取类名和方法名
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        //获取请求参数
        Object[] args = joinPoint.getArgs();

        //调用构造器，完成参数的传递
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);

        //记录日志信息
        logger.info("Request : {}", requestLog);
    }

    @After("log()")
    public void doAfter() {
        //logger.info("--------doAfter---------");
    }

    // 声明result时指定的类型会限制目标方法必须返回指定类型的值或没有返回值
    // 此处将result的类型声明为Object，意味着对目标方法的返回值不加限制
    @AfterReturning(returning = "result", pointcut = "log()")
    //利用形参 result 捕获被拦截方法所返回的内容
    public void doAfterReturn(Object result) {
        logger.info("Result : {}", result);
    }

    /**
     * 封装了日志内容
     */
    private class RequestLog {
        private String url; //请求路径
        private String ip;  //访问者ip
        private String classMethod; //调用方法
        private Object[] args;  //请求参数（url传递过来的参数）

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }

}
