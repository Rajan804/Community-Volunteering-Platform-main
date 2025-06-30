package com.cvp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/organization/**").permitAll()
                        .requestMatchers("/task/**").permitAll()

                        .requestMatchers("/users/**").permitAll()

                        .requestMatchers("/tasksignup/**").permitAll()
                        
                        .requestMatchers("/ratings/**").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }
}
