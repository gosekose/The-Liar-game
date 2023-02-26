package liar.resultservice.common.redis;

import liar.resultservice.exception.exception.RedisLockException;
import liar.resultservice.result.controller.dto.request.PlayerResultInfoDto;
import liar.resultservice.result.domain.GameResult;
import liar.resultservice.result.domain.GameRole;
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

    @Around(
//            "execution(* liar.resultservice.result.repository.PlayerRepository.save(..)) || " +
            "execution(* liar.resultservice.result.repository..*.delete(..))"
//            "execution(* liar.resultservice.other.member.MemberRepository.findByUserId(..))"
    )
    public Object executeWithRock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> returnType = method.getReturnType();

        String lockKey = getLockKey(joinPoint.getArgs());
        RLock lock = redissonClient.getLock(lockKey);

        try{
            boolean isLocked = lock.tryLock(10, 12, TimeUnit.SECONDS);
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
//
//    @Around("execution(* liar.resultservice.result.service.save.SavePolicyImpl.updatePlayer(..)) && args(gameResult, player, playerRole, exp)")
//    public void savePlayer(ProceedingJoinPoint joinPoint, GameResult gameResult, Player player, GameRole playerRole, Long exp) throws Throwable {
//
//        String lockKey = "savePlayer: " + player.getMember().getId();
//        voidJoinPointRedissonRLock(joinPoint, lockKey);
//    }
//
//    @Around("execution(* liar.resultservice.result.service.save.SavePolicyImpl.getPlayer(..)) && args(dto)")
//    public Player getPlayer(ProceedingJoinPoint joinPoint, PlayerResultInfoDto dto) throws Throwable {
//
//        String lockKey = "getPlayer: " + dto.getUserId();
//        return (Player) executeWithRedisLock(joinPoint, lockKey);
//    }

    public Object executeWithRedisLock(ProceedingJoinPoint joinPoint, String lockKey) throws Throwable {
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(20, 3, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new RedisLockException();
            }
            log.info("lockKey = {} 락 획득", lockKey);

            return joinPoint.proceed();

        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("lockKey = {} 락 반환", lockKey);
            }
        }
    }


    private void voidJoinPointRedissonRLock(ProceedingJoinPoint joinPoint, String lockKey) throws Throwable {
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(60, TimeUnit.SECONDS);
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
