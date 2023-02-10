package hello.community.global.aop;

import hello.community.global.log.LogTrace;
import hello.community.global.log.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LogAop {
    private final LogTrace logTrace;

    @Pointcut("execution(* hello.community..*Service*.*(..))")
    public void allService(){};

    @Pointcut("execution(* hello.community..*Repository*.*(..))")
    public void allRepository(){};

    @Pointcut("execution(* hello.community..*Controller*.*(..))")
    public void allController(){};

    @Around("allService() || allController() || allRepository()")
    public Object logTrace(ProceedingJoinPoint joinPoint) throws Throwable {

        TraceStatus status = null;

        try{
            status = logTrace.begin(joinPoint.getSignature().toShortString());
            Object result = joinPoint.proceed();

            logTrace.end(status);

            return result;
        }catch (Throwable e){
            e.printStackTrace();
            logTrace.exception(status, e);
            throw e;
        }
    }
}
