package liar.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/member-service/**")
                        .filters(f -> f
                                .rewritePath("/member-service/(<segment>.*)", "/$\\{segment}"))
                        .uri("http://127.0.0.1:8080"))
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
