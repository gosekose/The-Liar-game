package liar.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
    public GlobalFilter() {
        super(Config.class);
    }

    @Data
    public static class Config {
        // configuration 정보
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

    @Override
    public GatewayFilter apply(Config config) {
        // custom pre filter
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global Filter baseMessage = {}", config.getBaseMessage());
            log.info("request.getHeaders() = {}", request.getHeaders());
            log.info("request.getURI() = {}", request.getURI());

            if (config.isPreLogger()) {
                log.info("Global Filter Start: request id -> {}", config.getBaseMessage());
            }

            // Custom Post Filter

            return chain.filter(exchange).then(Mono.fromRunnable(

                    () -> {
                        if (config.isPostLogger()) {
                            log.info("Global Filter End: request id -> {}", config.getBaseMessage());
                        }
                    }));
        });
    }
}
