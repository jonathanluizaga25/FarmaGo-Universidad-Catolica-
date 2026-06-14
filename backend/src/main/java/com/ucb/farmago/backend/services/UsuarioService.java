package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.models.Usuario;
import com.ucb.farmago.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario registrar(Usuario usuario) {
        String email = usuario.getEmail();
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("El formato del email no es valido");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya esta registrado");
        }
        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        usuario.setRol("CLIENTE");
        return usuarioRepository.save(usuario);
    }

    public Usuario login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Correo o contraseña incorrectos"));
        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw new RuntimeException("Correo o contraseña incorrectos");
        }
        return usuario;
    }
}
