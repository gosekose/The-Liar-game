package liar.gamemvcservice.common.redis;

import liar.gamemvcservice.exception.exception.RedisLockException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Component
public class RedisLockAspect {

    private final RedissonClient redissonClient;

    @Around("execution(* liar.gamemvcservice.game.repository.redis..*.save(..)) || " +
            "execution(* liar.gamemvcservice.game.repository.redis..*.delete(..)) || " +
            "execution(* liar.gamemvcservice.game.service.GameService..*.updatePlayerTurnAndNotifyNextTurnWhenPlayerTurnIsValidated(..)) || " +
            "execution(* liar.gamemvcservice.game.service.GameService..*.save(..))")
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
            lock.unlock();
        }
    }

    private String getLockKey(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof String) {
                return (String) arg;
            }
        }
        throw new RedisLockException();

    }

}
