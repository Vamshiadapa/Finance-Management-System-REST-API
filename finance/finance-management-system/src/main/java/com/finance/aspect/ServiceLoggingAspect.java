package com.finance.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ServiceLoggingAspect {

    @Around("execution(* com.finance.service..*(..))")
    public Object logServiceExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - startTime;

        log.info("Service method: {} executed in {} ms",
                joinPoint.getSignature().getName(),
                executionTime);

        return result;
    }
}