package com.example.caso_1.api.controller;

import com.example.caso_1.api.config.AuthRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String SECRET_KEY = "123456789";

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody AuthRequest authRequest) {
        return Mono.just(authRequest)
                .flatMap(request -> authenticate(request.getUsername(), request.getPassword()))
                .map(user -> {
                    String token = Jwts.builder()
                            .setSubject(user.getUsername())
                            .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                            .compact();
                    Map<String, String> response = new HashMap<>();
                    response.put("token", token);
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(401).body(Map.of("error", e.getMessage()))));
    }

    private Mono<User> authenticate(String username, String password) {
        // Implement your actual authentication logic here
        if ("user".equals(username) && "password".equals(password)) {
            return Mono.just(new User(username, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
        } else {
            return Mono.error(new AuthenticationException("Invalid username or password") {});
        }
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Map<String, String>>> register(@RequestBody AuthRequest authRequest) {
        return Mono.just(authRequest)
                .flatMap(request -> registerUser(request.getUsername(), request.getPassword()))
                .map(user -> {
                    String token = Jwts.builder()
                            .setSubject(user.getUsername())
                            .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                            .compact();
                    Map<String, String> response = new HashMap<>();
                    response.put("token", token);
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(400).body(Map.of("error", e.getMessage()))));
    }

    private Mono<User> registerUser(String username, String password) {
        // Implement your actual registration logic here
        return Mono.just(new User(username, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
    }
}
