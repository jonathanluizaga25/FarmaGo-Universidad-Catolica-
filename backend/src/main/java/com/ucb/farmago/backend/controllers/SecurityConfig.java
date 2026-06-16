package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // --- REGLAS DEFINITIVAS (descomentar cuando el equipo confirme listo para activar) ---
                        // .requestMatchers("/api/auth/**").permitAll()
                        // .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        // .requestMatchers(HttpMethod.POST,   "/api/productos/**").hasRole("ADMINISTRADOR")
                        // .requestMatchers(HttpMethod.PUT,    "/api/productos/**").hasRole("ADMINISTRADOR")
                        // .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMINISTRADOR")
                        // .requestMatchers("/api/pedidos/**").authenticated()
                        // .requestMatchers("/api/proveedores/**").hasRole("ADMINISTRADOR")
                        // .requestMatchers("/api/lotes/**").hasRole("ADMINISTRADOR")
                        // .requestMatchers("/api/alertas/**").hasRole("ADMINISTRADOR")
                        // .requestMatchers("/api/caja/**").hasRole("ADMINISTRADOR")
                        // .requestMatchers("/api/descuentos/**").hasRole("ADMINISTRADOR")
                        // .anyRequest().authenticated()
                        // -----------------------------------------------------------------

                        .anyRequest().permitAll() // ← se mantiene por ahora, igual que en el PR de A
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}