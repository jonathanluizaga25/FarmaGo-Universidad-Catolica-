package com.ucb.farmago.backend.controllers;

// ─── SecurityConfig — El portero del backend ──────────────────────────────────
// Define las reglas de acceso para TODOS los endpoints del sistema.
// Funciona junto con JwtAuthFilter: el filtro identifica al usuario,
// y este archivo decide si ese usuario tiene permiso para la ruta que pidió.
//
// Reglas en orden de prioridad (Spring evalúa de arriba hacia abajo):
//   OPTIONS /**          → siempre permitido (necesario para CORS del navegador)
//   /api/auth/**         → público (login y registro no requieren token)
//   GET /api/productos/** → público (el catálogo lo ve cualquiera)
//   POST/PUT/DELETE productos, alertas, lotes, caja, etc. → solo ADMINISTRADOR
//   /api/pedidos/**      → cualquier usuario autenticado (cliente, admin, repartidor)
//   cualquier otra ruta  → autenticado (requiere token válido)

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter; // nuestro filtro JWT personalizado

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Desactivar CSRF: no aplica a APIs REST stateless (sin cookies de sesión)
                .csrf(csrf -> csrf.disable())

                // Configurar CORS: permite que el frontend en localhost:3000 llame al backend
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Sin sesiones HTTP: cada request se autentica con el token JWT solamente
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ── Reglas de acceso por ruta ─────────────────────────────────
                .authorizeHttpRequests(auth -> auth

                        // Preflight CORS: el navegador envía OPTIONS antes de cada request
                        // con Authorization. Si esto da 403, el frontend ve "Load failed".
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Rutas públicas (sin token)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()

                        // Solo ADMINISTRADOR puede modificar el inventario y datos sensibles
                        .requestMatchers(HttpMethod.POST,   "/api/productos/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT,    "/api/productos/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/alertas/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/lotes/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/caja/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/descuentos/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/proveedores/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/acuerdos/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR")

                        // Pedidos e historial: cualquier usuario logueado (cliente o admin)
                        .requestMatchers("/api/pedidos/**").authenticated()
                        .requestMatchers("/api/historial/**").authenticated()

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                // Agregar nuestro filtro JWT ANTES del filtro de usuario/contraseña de Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ── corsConfigurationSource ───────────────────────────────────────────────
    // Sin esto, el navegador bloquea las respuestas del backend por política de
    // seguridad Same-Origin. CORS le dice al navegador qué orígenes están permitidos.
    // El problema del "Load failed" era porque OPTIONS daba 403 antes de llegar aquí.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*")); // cualquier origen (dev)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*")); // incluye Authorization
        config.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // aplica a todas las rutas
        return source;
    }
}
