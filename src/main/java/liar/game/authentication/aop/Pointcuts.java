package liar.game.authentication.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
public class Pointcuts {

    @Pointcut("execution(* liar.game.authentication..*.*(..))")
    public void allAuthentication() {}


}
