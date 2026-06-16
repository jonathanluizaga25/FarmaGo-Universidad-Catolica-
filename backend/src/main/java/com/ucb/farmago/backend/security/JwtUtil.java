package com.ucb.farmago.backend.security;

// ─── JwtUtil — Generador y validador de tokens JWT ───────────────────────────
// JWT (JSON Web Token) es una cadena firmada que prueba que el usuario se autenticó.
// Formato: HEADER.PAYLOAD.FIRMA  (todo en Base64)
// Ejemplo: eyJhbGci....eyJzdWIi....lP29qx...
// El backend genera el token al hacer login y el frontend lo guarda en localStorage.
// En cada request protegido, el frontend lo envía en el header Authorization.

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Clave secreta leída desde application.properties (nunca hardcodeada en el código)
    @Value("${jwt.secret}")
    private String secret;

    // Tiempo de expiración en milisegundos (86400000 = 24 horas)
    @Value("${jwt.expiration}")
    private long expiration;

    // Convierte la clave string en un objeto criptográfico para firmar tokens
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ── generarToken ──────────────────────────────────────────────────────────
    // Se llama en AuthController.login() después de verificar usuario y contraseña.
    // Crea un token con:
    //   - subject: el email del usuario (lo identifica)
    //   - claim "rol": ADMINISTRADOR, CLIENTE o REPARTIDOR
    //   - issuedAt: fecha de creación
    //   - expiration: fecha de vencimiento (ahora + 24h)
    // Todo firmado con HMAC-SHA384 para que nadie pueda falsificarlo.
    public String generarToken(String email, String rol) {
        Date ahora = new Date();
        Date expira = new Date(ahora.getTime() + expiration);

        return Jwts.builder()
                .subject(email)
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(expira)
                .signWith(getSigningKey())
                .compact();
    }

    // ── validarYLeer ──────────────────────────────────────────────────────────
    // Se llama en JwtAuthFilter en cada request.
    // Verifica la firma del token (si fue alterado, lanza excepción).
    // Verifica que no esté expirado.
    // Devuelve los Claims (datos) del token: email, rol, fechas.
    public Claims validarYLeer(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Métodos de conveniencia para extraer datos específicos del token
    public String obtenerEmail(String token) {
        return validarYLeer(token).getSubject();
    }

    public String obtenerRol(String token) {
        return validarYLeer(token).get("rol", String.class);
    }
}
