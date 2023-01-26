package liar.gateway.config;

import liar.gateway.filter.AuthorizationHeaderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final AuthorizationHeaderFilter authorizationHeaderFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, AuthorizationHeaderFilter authorizationHeaderFilter) {
        return builder.routes()
                .route("member-service", r -> r
                        .path("/member-service/**")
//                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://member-service")
                )

                .route("member-service", r -> r
                        .path("/member-service/users/**")
                        .filters(spec -> spec.filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config())))
                        .uri("lb://member-service"))

                .build();
    }


//    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(r -> r.path("/member-service/**")
//                        .filters(f -> f
//                                .addRequestHeader("first-request", "first-request-header")
//                                .rewritePath("/member-service/(<segment>.*)", "/$\\{segment}")
//                                .addResponseHeader("first-response-header", "first-response-header"))
//                        .uri("http://127.0.0.1:8080"))
//                .build();
//    }



}
