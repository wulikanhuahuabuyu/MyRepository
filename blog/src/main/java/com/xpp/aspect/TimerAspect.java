package com.xpp.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimerAspect {

    @Around("execution(* com.xpp.service.*.*(..))")
    public Object a(ProceedingJoinPoint pjp) throws Throwable{
        //记录开始时间
        Long st = System.currentTimeMillis();

        //调用目标方法
        Object result = pjp.proceed();

        //记录结束时间
        Long et = System.currentTimeMillis();

        //输出耗时
        System.err.println(pjp.getSignature().getName()+"->耗时："+(st-et)+"ms.");

        return result;
    }
}
