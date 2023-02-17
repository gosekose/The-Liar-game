package liar.gamemvcservice.common.config;

import liar.gamemvcservice.game.controller.interceptor.UserInterceptor;
import liar.gamemvcservice.game.controller.interceptor.VoteInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebMvc
public class InterceptorConfig implements WebMvcConfigurer {

    private final UserInterceptor userInterceptor;
    private final VoteInterceptor voteInterceptor;

    private static final String BASE_PATH = "/game-service/game";
    private static final String[] USER_INTERCEPTOR_PATH = {
            "/{userId}/role", "/{userId}/topic", "/{userId}/turn",
    };

    private static final String[] VOTE_INTERCEPTOR_PATH = {
            "/{userId}/vote",
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns(buildPathPatterns(USER_INTERCEPTOR_PATH))
                .addPathPatterns(buildPathPatterns("/exclude_id", USER_INTERCEPTOR_PATH));

        registry.addInterceptor(voteInterceptor)
                .addPathPatterns(buildPathPatterns(VOTE_INTERCEPTOR_PATH))
                .addPathPatterns(buildPathPatterns("/exclude_id", VOTE_INTERCEPTOR_PATH));
    }

    private String[] buildPathPatterns(String[] subPaths) {
        return Arrays.stream(subPaths)
                .map(subPath -> BASE_PATH + subPaths)
                .toArray(String[]::new);
    }

    private String[] buildPathPatterns(String excludeId, String[] subPaths) {
        return Arrays.stream(subPaths)
                .map(subPath -> BASE_PATH + subPath)
                .map(path -> path.replace("{userId}", excludeId))
                .toArray(String[]::new);
    }
}
