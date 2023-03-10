package com.ljq.aspect;

import com.alibaba.fastjson.JSON;
import com.ljq.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Slf4j
@Component
public class LogAspect {

    @Pointcut("@annotation(com.ljq.annotation.SystemLog)")
    public void pt(){}

    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {

        Object res;
        try {
            handlerBefore(joinPoint);
            res = joinPoint.proceed();
            handlerAfter(res);
        } finally {
            log.info("=======End=======" + System.lineSeparator());
        }
        return res;
    }

    private void handlerAfter(Object res) {
        // ζε°εΊε
        log.info("Response       : {}", JSON.toJSONString(res));
    }

    private SystemLog getSystemLog(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(SystemLog.class);
    }

    private void handlerBefore(ProceedingJoinPoint joinPoint) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        SystemLog systemLog = getSystemLog(joinPoint);

        log.info("=======Start=======");
        // ζε°θ―·ζ± URL
        log.info("URL            : {}",request.getRequestURL());
        // ζε°ζθΏ°δΏ‘ζ―
        log.info("BusinessName   : {}",systemLog.bussinessName());
        // ζε° Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // ζε°θ°η¨ controller ηε¨θ·―εΎδ»₯εζ§θ‘ζΉζ³
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName());
        // ζε°θ―·ζ±η IP
        log.info("IP             : {}",request.getRemoteHost());
        // ζε°θ―·ζ±ε₯ε
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

}
