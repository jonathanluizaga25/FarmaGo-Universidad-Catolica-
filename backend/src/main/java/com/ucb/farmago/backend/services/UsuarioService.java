package com.ucb.farmago.backend.services;

import org.springframework.transaction.annotation.Transactional;
import com.ucb.farmago.backend.models.Alerta;
import com.ucb.farmago.backend.models.Usuario;
import com.ucb.farmago.backend.repositories.AlertaRepository;
import com.ucb.farmago.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AlertaRepository alertaRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
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
        Usuario guardado = usuarioRepository.save(usuario);

        // Alerta al administrador cuando se registra un nuevo usuario
        Alerta alerta = new Alerta();
        alerta.setTipo("NUEVO_USUARIO");
        alerta.setMensaje("Nuevo usuario registrado: " + guardado.getNombre() +
                " (" + guardado.getEmail() + ")");
        alerta.setLeida(Boolean.FALSE);
        alertaRepository.save(alerta);

        return guardado;
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