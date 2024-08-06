package com.example.caso_1.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JWTAuthenticationManager jwtAuthenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public SecurityConfig(JWTAuthenticationManager jwtAuthenticationManager, SecurityContextRepository securityContextRepository) {
        this.jwtAuthenticationManager = jwtAuthenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

   @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/auth/**", "/h2-console/**", "/swagger-ui**").permitAll()
                        .anyExchange().authenticated()
                )
                .authenticationManager(jwtAuthenticationManager)
                .securityContextRepository(securityContextRepository)
                .build();
    }


}
