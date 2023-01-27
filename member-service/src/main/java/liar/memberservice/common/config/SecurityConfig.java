package liar.memberservice.common.config;

import liar.memberservice.authentication.service.CustomOAuth2UserService;
import liar.memberservice.authentication.service.CustomOidcUserService;
import liar.memberservice.token.controller.filter.AuthenticationGiveFilter;
import liar.memberservice.token.domain.TokenProviderImpl;
import liar.memberservice.token.repository.TokenRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final CorsFilter corsFilter;
    private final TokenProviderImpl tokenProviderImpl;
    private final TokenRepositoryImpl tokenRepository;

    private static final String[] webServiceWhiteList = {
            "/static/**", "/static/js/**", "/static/images/**",
            "/static/css/**", "/static/scss/**", "/static/docs/**",
            "/h2-console/**", "/favicon.ico", "/error"
    };

    private static final String[] memberServiceWhiteList = {
            "/member-service/**", "/", "**/member-service/**"
    };


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(webServiceWhiteList);
    }


    @Bean
    SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .csrf(csrf -> csrf.disable())
                .cors().disable()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .headers()
                .xssProtection()
                .and()
                .contentSecurityPolicy("script-src 'self'")
                .and()
                .frameOptions()
                .sameOrigin()

                .and()
                .anonymous()
                .and()
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers(memberServiceWhiteList)
                .permitAll())
                .authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()

                .formLogin().disable()

                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(
                        userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)
                                .oidcUserService(customOidcUserService)))

                // SecurityConfig FilterChain
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationGiveFilter(), UsernamePasswordAuthenticationFilter.class);


        return http.build();

    }

//    @Bean
//    public JwtFilter jwtFilter() {
//        return new JwtFilter(tokenProviderImpl, tokenRepository);
//    }

    @Bean
    public AuthenticationGiveFilter authenticationGiveFilter() { return new AuthenticationGiveFilter(tokenProviderImpl);}
}

