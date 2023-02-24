package liar.resultservice.common.redis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RedissonCallTransaction {
    /**
     * 부모트랜잭션의 유무와 관계없이 동시성에 대한 처리는 별도의 트랜잭션으로 동작하기 위함
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}