package liar.waitservice.common.redis;

import liar.waitservice.exception.exception.RedisLockException;
import liar.waitservice.wait.controller.dto.CreateWaitRoomDto;
import liar.waitservice.wait.controller.dto.RequestWaitRoomDto;
import liar.waitservice.wait.domain.JoinMember;
import liar.waitservice.wait.domain.WaitRoom;
import liar.waitservice.wait.domain.WaitRoomComplete;
import liar.waitservice.wait.domain.WaitRoomCompleteJoinMember;
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

    @Around("execution(* liar.waitservice.wait.service.WaitRoomFacadeService.saveWaitRoomByHost(..)) && args(createWaitRoomDto)")
    public String saveWaitRoomByHostWithRedisLock(ProceedingJoinPoint joinPoint, CreateWaitRoomDto createWaitRoomDto) throws Throwable {
        String lockKey = "saveWaitRoomByHost: " + createWaitRoomDto.getUserId();
        return (String) executeWithRedisLock(joinPoint, lockKey);
    }

    @Around("execution(* liar.waitservice.wait.service.WaitRoomFacadeService.addMembers(..)) && args(dto)")
    public boolean addMembersWithRedisLock(ProceedingJoinPoint joinPoint, RequestWaitRoomDto dto) throws Throwable {
        String lockKey = "addMembers: " + dto.getRoomId();
        return (boolean) executeWithRedisLock(joinPoint, lockKey);
    }

    @Around("execution(* liar.waitservice.wait.service.WaitRoomFacadeService.leaveMember(..)) && args(dto)")
    public boolean leaveMemberWithRedisLock(ProceedingJoinPoint joinPoint, RequestWaitRoomDto dto) throws Throwable {
        String lockKey = "leaveMember: " + dto.getRoomId();
        return (boolean) executeWithRedisLock(joinPoint, lockKey);
    }

    @Around("execution(* liar.waitservice.wait.service.WaitRoomFacadeService.deleteWaitRoomByHost(..)) && args(dto)")
    public boolean deleteWaitRoomByHostWithRedisLock(ProceedingJoinPoint joinPoint, RequestWaitRoomDto dto) throws Throwable {
        String lockKey = "deleteWaitRoomByHost: " + dto.getRoomId();
        return (boolean) executeWithRedisLock(joinPoint, lockKey);
    }

    public Object executeWithRedisLock(ProceedingJoinPoint joinPoint, String lockKey) throws Throwable {
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(30, TimeUnit.SECONDS);
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

        else if (arg instanceof WaitRoom){
            return ((WaitRoom) arg).getId();
        }

        else if (arg instanceof JoinMember) {
            return ((JoinMember) arg).getId();
        }

        else if (arg instanceof WaitRoomComplete) {
            return ((WaitRoomComplete) arg).getId();
        }

        else if (arg instanceof WaitRoomCompleteJoinMember) {
            return ((WaitRoomCompleteJoinMember) arg).getId();
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
