package com.example.caso_1.api.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JWTAuthenticationManager implements ReactiveAuthenticationManager {

    private static final String SECRET_KEY = "123456789";

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(authToken)
                    .getBody();

            if (claims.getExpiration().before(new Date())) {
                return Mono.empty();
            }

            String username = claims.getSubject();
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            User user = new User(username, "", authorities);
            return Mono.just(new UsernamePasswordAuthenticationToken(user, null, authorities));
        } catch (Exception e) {
            return Mono.error(new AuthenticationException("Invalid token") {});
        }
    }
}
