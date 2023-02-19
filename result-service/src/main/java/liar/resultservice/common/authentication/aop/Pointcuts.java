package liar.resultservice.common.authentication.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
public class Pointcuts {

    @Pointcut("execution(* liar.resultservice.common.authentication..*.*(..))")
    public void allAuthentication() {}


}
