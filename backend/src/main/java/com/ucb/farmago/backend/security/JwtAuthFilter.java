package com.ucb.farmago.backend.security;

// ─── JwtAuthFilter — Filtro que se ejecuta en CADA petición HTTP ──────────────
// Spring Security tiene una cadena de filtros que revisan cada request antes
// de que llegue al controlador. Este filtro está agregado al inicio de esa cadena.
//
// Flujo de una petición con token:
//   1. Navegador envía: GET /api/alertas + Header: "Authorization: Bearer eyJ..."
//   2. Este filtro lee el header, extrae el token
//   3. JwtUtil valida la firma y extrae email + rol
//   4. El filtro crea un objeto Authentication con esos datos
//   5. Lo pone en el SecurityContext (contexto de seguridad de Spring)
//   6. SecurityConfig compara el rol con las reglas y decide si deja pasar
//   7. Si pasa → llega al AlertaController → devuelve datos
//
// Flujo sin token (o token inválido):
//   1. El filtro no encuentra header válido → no crea Authentication
//   2. SecurityConfig ve que no hay autenticación → 403 Forbidden

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// OncePerRequestFilter garantiza que este filtro se ejecuta UNA SOLA VEZ por request
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Paso 1: leer el header Authorization del request
        String header = request.getHeader("Authorization");

        // Paso 2: verificar que empiece con "Bearer " (estándar OAuth2/JWT)
        if (header != null && header.startsWith("Bearer ")) {
            // Paso 3: extraer el token (quita los primeros 7 caracteres "Bearer ")
            String token = header.substring(7);

            try {
                // Paso 4: validar el token y extraer sus datos
                Claims claims = jwtUtil.validarYLeer(token);
                String email = claims.getSubject();           // quién es
                String rol = claims.get("rol", String.class); // qué puede hacer

                // Paso 5: Spring Security usa el prefijo "ROLE_" internamente
                // hasRole("ADMINISTRADOR") en SecurityConfig busca "ROLE_ADMINISTRADOR"
                var authority = new SimpleGrantedAuthority("ROLE_" + rol);

                // Paso 6: crear el objeto de autenticación (usuario verificado)
                var authentication = new UsernamePasswordAuthenticationToken(
                        email, null, List.of(authority));

                // Paso 7: registrar en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Token inválido o expirado: limpiar el contexto y dejar que
                // SecurityConfig decida si la ruta es pública o requiere auth
                SecurityContextHolder.clearContext();
            }
        }

        // Paso 8: continuar con el siguiente filtro en la cadena (siempre)
        filterChain.doFilter(request, response);
    }
}
