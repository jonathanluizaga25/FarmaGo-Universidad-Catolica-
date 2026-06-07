package com.ucb.farmago.backend.config;

import com.ucb.farmago.backend.models.Usuario;
import com.ucb.farmago.backend.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedAdmin(UsuarioRepository repo) {
        return args -> {
            crearAdminSiNoExiste(repo, "admin@farmago.com", "admin1234", "Administrador");
        };
    }

    private void crearAdminSiNoExiste(UsuarioRepository repo, String email, String password, String nombre) {
        if (repo.findByEmail(email).isPresent()) {
            return;
        }
        Usuario admin = new Usuario();
        admin.setNombre(nombre);
        admin.setEmail(email);
        admin.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        admin.setRol("ADMIN");
        admin.setDireccion("");
        admin.setTelefono("");
        repo.save(admin);
        System.out.println("✓ Admin creado → email: " + email + " | contraseña: " + password);
    }
}
