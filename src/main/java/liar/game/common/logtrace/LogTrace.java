package liar.game.common.logtrace;

import org.springframework.stereotype.Component;

@Component
public interface LogTrace {

    TraceStatus begin(String email, String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);

}
