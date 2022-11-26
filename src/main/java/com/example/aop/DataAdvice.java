package com.example.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class DataAdvice {
    @Pointcut("execution(* com.example.controller.UserController.*(..))")
    private void pt(){}

    @Around("pt()")
    public Object trimParams(ProceedingJoinPoint pjp) throws Throwable {
        Object[] params = pjp.getArgs();
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof String){
                params[i] = ((String) params[i]).trim();
            }
        }

        Object ret = pjp.proceed(params);

        return ret;
    }
}
