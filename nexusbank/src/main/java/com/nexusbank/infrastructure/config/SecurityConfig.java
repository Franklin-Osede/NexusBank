package com.nexusbank.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // Permitir acceso a la documentación OpenAPI
            .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            // Permitir acceso a la consola H2
            .requestMatchers("/h2-console/**").permitAll()
            // Para fines de desarrollo, permitimos todas las rutas. En producción, esto
            // debería ser más restrictivo
            .requestMatchers("/**").permitAll())
        // Configurar para la consola H2
        .headers(headers -> headers.frameOptions().disable())
        // Uso de autenticación sin estado
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}