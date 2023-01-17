package liar.game.common.logtrace;

import lombok.Getter;
import org.springframework.security.core.Authentication;

@Getter
public class TraceId {

    private String id;
    private int level;

    public TraceId(String email) {
        this.id = createId(email);
        this.level = 0;
    }

    public TraceId(String email, int level) {
        this.id = email;
        this.level = level;
    }

    private String createId(String email) {

        if (email.length() >= 8) {
            return email.substring(0, 8);
        }

        for (int i = 0; i < 8 - email.length(); i++) {
            email += " ";
        }
        return email;
    }

    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }

}
