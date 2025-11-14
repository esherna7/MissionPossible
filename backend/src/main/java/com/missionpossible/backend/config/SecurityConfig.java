package com.missionpossible.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF safely in modern Spring Security
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // Allow all requests
        return http.build();
    }
}

//the secruity config files allows all get, post, put, delete methods
//it removes the need to login