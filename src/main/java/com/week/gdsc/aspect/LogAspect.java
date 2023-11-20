package com.week.gdsc.aspect;

import com.week.gdsc.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @AfterReturning(value = "execution(* com.week.gdsc.service.UserService.getByCredentials(..))",returning = "returnUser")
    public void logAfterCredential(JoinPoint joinPoint,User returnUser){
        if (returnUser!=null){
            log.info("getByCredentials: 사용자 인증 완료");
        }
    }

    @Before("execution(* com.week.gdsc.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("메소드 실행 전: " + joinPoint.getSignature().getName());
    }

    @After("execution(* com.week.gdsc.service.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        log.info("메소드 실행 후: " + joinPoint.getSignature().getName());
    }

    @Around("execution(* com.week.gdsc.controller.*.*(..))")
    public Object measureMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime=System.currentTimeMillis();

        Object returnValue=joinPoint.proceed();

        long endTime=System.currentTimeMillis();

        long executionTime=endTime-startTime;

        String methodName=joinPoint.getSignature().getName();

        log.info(methodName+"실행 시간: "+executionTime);

        return returnValue;

    }
}
