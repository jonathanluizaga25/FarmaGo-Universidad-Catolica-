package com.ucb.farmago.backend.controllers;

// ─── AuthController — Endpoints de autenticación ─────────────────────────────
// Maneja el registro, login y listado de usuarios.
// Es el único controlador completamente público (/api/auth/**).
//
// Endpoints:
//   POST /api/auth/registro  → crea una cuenta nueva (CLIENTE por defecto)
//   POST /api/auth/login     → verifica credenciales y devuelve { token, usuario }
//   GET  /api/auth/usuarios  → lista todos los usuarios (solo ADMIN, ver SecurityConfig)

import com.ucb.farmago.backend.dto.UsuarioDTO;
import com.ucb.farmago.backend.models.Usuario;
import com.ucb.farmago.backend.security.JwtUtil;
import com.ucb.farmago.backend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Autenticacion")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil; // para generar el token después del login

    // ── POST /api/auth/registro ───────────────────────────────────────────────
    // Recibe los datos del formulario de registro y crea el usuario en la BD.
    // UsuarioService.registrar() verifica que el email no esté repetido.
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioDTO(nuevo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── GET /api/auth/usuarios ────────────────────────────────────────────────
    // Lista todos los usuarios del sistema. Usado por el panel admin → sección Usuarios.
    // SecurityConfig restringe este endpoint a rol ADMINISTRADOR.
    @GetMapping("/usuarios")
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(
            usuarioService.listarTodos()
                .stream()
                .map(UsuarioDTO::new) // convierte Usuario (entidad) a UsuarioDTO (sin passwordHash)
                .collect(Collectors.toList())
        );
    }

    // ── POST /api/auth/login ──────────────────────────────────────────────────
    // 1. Recibe { email, password }
    // 2. UsuarioService verifica las credenciales contra la BD
    // 3. JwtUtil genera un token firmado con el email y rol del usuario
    // 4. Devuelve { token, usuario } al frontend
    // El frontend guarda el token en localStorage y lo usa en fetchWithAuth()
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            Usuario usuario = usuarioService.login(body.get("email"), body.get("password"));
            String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());

            // Construir respuesta con ambos datos
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("token", token);
            respuesta.put("usuario", new UsuarioDTO(usuario)); // sin contraseña

            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            // Email no existe o contraseña incorrecta → 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
