package liar.waitservice.common.redis;

import liar.waitservice.exception.exception.RedisLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class RedisLockAspect {

    private final RedissonClient redissonClient;
//
//    @Around(
//            "execution(* liar.waitservice.wait.repository.redis..*.save(..)) || " +
//            "execution(* liar.waitservice.wait.repository.redis..*.delete(..)) || " +
//            "execution(* liar.waitservice.wait.repository.redis..*.findVoteByGameId(..)) || " +
//            "execution(* liar.waitservice.wait.repository.redis..*.findByGameId(..)) || " +
//            "execution(* liar.waitservice.wait.repository.redis..*.findById(..)) || " +
//            "execution(* liar.waitservice.wait.service.vote..*.saveVote(..))"
//    )
//    public Object executeWithRock(ProceedingJoinPoint joinPoint) throws Throwable {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        Class<?> returnType = method.getReturnType();
//
//        String lockKey = getLockKey(joinPoint.getArgs());
//        RLock lock = redissonClient.getLock(lockKey);
//
//        try{
//            boolean isLocked = lock.tryLock(2, 3, TimeUnit.SECONDS);
//            if (!isLocked) {
//                throw new RedisLockException();
//            }
//            Object result = joinPoint.proceed();
//            return returnType.cast(result);
//
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }

//    @Around("execution(* liar.waitservice.wait.service.vote.VotePolicy.voteLiarUser(..)) && args(gameId, userId, liarId)")
//    public boolean voteLiarUserWithRedisLock(ProceedingJoinPoint joinPoint, String gameId, String userId, String liarId) throws Throwable {
//        String lockKey = "VoteLiarUser: " + gameId;
//        return (boolean) executeWithRedisLock(joinPoint, lockKey);
//    }
//
//    @Around("execution(* liar.gamemvcservice.game.service.GameFacadeService.sendGameResultToServer(..)) && args(gameId)")
//    public GameResultToServerDto messageGameResultWithRedisLock(ProceedingJoinPoint joinPoint, String gameId) throws Throwable {
//        String lockKey = "messageGameResult: " + gameId;
//        return (GameResultToServerDto) executeWithRedisLock(joinPoint, lockKey);
//    }
//
//
//    @Around("execution(* liar.gamemvcservice.game.service.turn.PlayerTurnPolicy.setUpTurn(..)) && args(game)")
//    public GameTurn setUpTurn(ProceedingJoinPoint joinPoint, Game game) throws Throwable {
//        String lockKey = "setUpTurn: " + game.getId();
//        return (GameTurn) executeWithRedisLock(joinPoint, lockKey);
//    }

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
//
//    private <T> String getLockKey(T arg) {
//
//        if (arg instanceof String) {
//            return (String) arg;
//        }
//
//        else if (arg instanceof Game){
//            return ((Game) arg).getId();
//        }
//
//        else if (arg instanceof GameTurn) {
//            return ((GameTurn) arg).getId();
//        }
//
//        else if (arg instanceof Vote) {
//            return ((Vote) arg).getId();
//        }
//
//        else if (arg instanceof JoinPlayer) {
//            return ((JoinPlayer) arg).getId();
//        }
//
//        else if (arg instanceof Object[]) {
//            StringBuilder sb = new StringBuilder();
//            for (Object obj : (Object[]) arg) {
//                sb.append(getLockKey(obj));
//            }
//            return sb.toString();
//        }
//        throw new RedisLockException();
//    }

}
