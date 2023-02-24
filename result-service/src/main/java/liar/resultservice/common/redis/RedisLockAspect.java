package liar.resultservice.common.redis;

import liar.resultservice.exception.exception.RedisLockException;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.Player;
import liar.resultservice.result.domain.PlayerResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class RedisLockAspect {

    private final RedissonClient redissonClient;
    private final RedissonCallTransaction redissonCallTransaction;

    @Around(
            "execution(* liar.resultservice.result.repository..*.save(..)) || " +
            "execution(* liar.resultservice.result.repository..*.delete(..)) || " +
            "execution(* liar.resultservice.result.repository..*.findById(..))"
    )
    public Object executeWithRock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> returnType = method.getReturnType();

        String lockKey = getLockKey(joinPoint.getArgs());
        RLock lock = redissonClient.getLock(lockKey);

        try{
            boolean isLocked = lock.tryLock(2, 3, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new RedisLockException();
            }
            Object result = joinPoint.proceed();
            return returnType.cast(result);

        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Around("@annotation(liar.resultservice.common.redis.RedissonLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);

        /* create key */
        String key = this.createKey(signature.getParameterNames(), joinPoint.getArgs(), redissonLock.key());

        /* get rLock 객체 */
        RLock lock = redissonClient.getLock(key);

        try {
            /* get lock */
            boolean isPossible = lock.tryLock(redissonLock.waitTime(), redissonLock.leaseTime(), redissonLock.timeUnit());
            if (!isPossible) {
                return false;
            }

            log.info("Redisson Lock Key : {}", key);

            /* service call */
            return redissonCallTransaction.proceed(joinPoint);
        } catch (Exception e) {
            throw new InterruptedException();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * Redisson Key Create
     * @param parameterNames
     * @param args
     * @param key
     * @return
     */
    private String createKey(String[] parameterNames, Object[] args, String key) {
        String resultKey = key;

        /* key = parameterName */
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(key)) {
                resultKey += args[i];
                break;
            }
        }

        return resultKey;
    }


    public Object executeWithRedisLock(ProceedingJoinPoint joinPoint, String lockKey) throws Throwable {
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(2, 3, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new RedisLockException();
            }
            return joinPoint.proceed();

        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    private void voidJoinPointRedissonRLock(ProceedingJoinPoint joinPoint, String lockKey) throws Throwable {
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(2, 3, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new RedisLockException();
            }
            joinPoint.proceed();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private <T> String getLockKey(T arg) {

        if (arg instanceof String) {
            return (String) arg;
        }

        else if (arg instanceof GameResult){
            return ((GameResult) arg).getId();
        }

        else if (arg instanceof Player) {
            return ((Player) arg).getId();
        }

        else if (arg instanceof PlayerResult) {
            return ((PlayerResult) arg).getId();
        }

        else if (arg instanceof Object[]) {
            StringBuilder sb = new StringBuilder();
            for (Object obj : (Object[]) arg) {
                sb.append(getLockKey(obj));
            }
            return sb.toString();
        }
        throw new RedisLockException();
    }

}
