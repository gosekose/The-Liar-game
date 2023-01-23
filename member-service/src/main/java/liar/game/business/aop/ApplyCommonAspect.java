package liar.game.business.aop;

import liar.game.common.logtrace.LogTrace;
import liar.game.common.logtrace.TraceStatus;
import liar.game.member.domain.Member;
import liar.game.member.repository.MemberRepository;
import liar.game.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Component
public class ApplyCommonAspect {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    @Order(1)
    @Component
    @RequiredArgsConstructor
    @Aspect
    public static class LoginMemberLogTrace {

        private final LogTrace logTrace;

        @Around("liar.game.business.aop.BusinessCommonPointcuts.loginMemberLogTrace()")
        public Object loginMemberLogTrace(ProceedingJoinPoint joinPoint) throws Throwable {
            TraceStatus status = null;

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                String message = joinPoint.getSignature().toShortString();
                status = logTrace.begin(email, message);
                
                Object result = joinPoint.proceed();
                logTrace.end(status);

                return result;

            } catch (Exception e) {
                throw e;
            }
        }
    }
}
