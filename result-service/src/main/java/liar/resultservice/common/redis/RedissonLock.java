package liar.resultservice.common.redis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLock {
    String key();

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    // wait time : wait time 동안 lock 획득을 시도하고, 이 시간이 초과되면 lock 획득에 실패하고 false를 리턴한다.
    long waitTime() default 5L;

    // lease time : lock 획득에 성공한 이후, lease time 이 지나면 자동으로 lock을 해제한다.
    long leaseTime() default 3L;
}