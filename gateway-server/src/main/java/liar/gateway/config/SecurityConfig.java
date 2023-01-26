package liar.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/static/**", "/static/js/**", "/static/images/**",
                "/static/css/**", "/static/scss/**", "/static/docs/**",
                "/h2-console/**", "/favicon.ico", "/error"
        );
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .csrf().disable()

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
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/member-service/test")
                        .permitAll())
                .authorizeHttpRequests()
                .anyRequest().authenticated();


        return http.build();

    }

}