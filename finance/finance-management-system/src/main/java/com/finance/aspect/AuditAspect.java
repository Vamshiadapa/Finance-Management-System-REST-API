package com.finance.aspect;

import com.finance.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;

    @Around("execution(* com.finance.service..*(..)) && !execution(* com.finance.service.AuditLogService.*(..))")
    public Object auditServiceOperation(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = joinPoint.proceed();

        String methodName = joinPoint.getSignature().getName();

        if (methodName.equals("save")
                || methodName.equals("delete")
                || methodName.equals("approve")
                || methodName.equals("repay")) {

            String entityName = joinPoint.getTarget()
                    .getClass()
                    .getSimpleName();

            String action = methodName.toUpperCase();

            auditLogService.save(
                    action,
                    entityName,
                    null,
                    "Service operation executed successfully"
            );
        }

        return result;
    }
}

