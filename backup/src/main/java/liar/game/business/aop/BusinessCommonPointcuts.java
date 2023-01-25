package liar.game.business.aop;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
public class BusinessCommonPointcuts {

    @Pointcut("execution(* liar.game.business..*.*(..))")
    public void loginMemberLogTrace() {};

}
