package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.dto.UsuarioDTO;
import com.ucb.farmago.backend.models.Usuario;
import com.ucb.farmago.backend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioDTO(nuevo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            Usuario usuario = usuarioService.login(body.get("email"), body.get("password"));
            return ResponseEntity.ok(new UsuarioDTO(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
