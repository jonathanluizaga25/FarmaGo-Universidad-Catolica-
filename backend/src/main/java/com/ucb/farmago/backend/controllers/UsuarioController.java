package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.dto.UsuarioDTO;
import com.ucb.farmago.backend.models.Usuario;
import com.ucb.farmago.backend.repositories.UsuarioRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Usuarios")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar(@RequestParam(required = false) String rol) {
        List<Usuario> usuarios = (rol != null && !rol.isBlank())
                ? usuarioRepository.findByRol(rol.trim().toUpperCase())
                : usuarioRepository.findAll();

        return ResponseEntity.ok(
                usuarios.stream().map(UsuarioDTO::new).collect(Collectors.toList())
        );
    }
}
