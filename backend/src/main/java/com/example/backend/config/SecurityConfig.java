package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF は開発中は一旦無効化（あとで必要に応じて見直す）
            .csrf(AbstractHttpConfigurer::disable)
            // 全てのリクエストを許可
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // デフォルトのログインフォームを無効化
            .formLogin(AbstractHttpConfigurer::disable)
            // Basic認証も無効化
            .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
