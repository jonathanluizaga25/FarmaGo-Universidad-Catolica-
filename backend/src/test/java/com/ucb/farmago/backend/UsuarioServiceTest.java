package com.ucb.farmago.backend;
import com.ucb.farmago.backend.models.Usuario;
import com.ucb.farmago.backend.repositories.UsuarioRepository;
import com.ucb.farmago.backend.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void login_conPasswordIncorrecta_lanzaError() {
        Usuario u = new Usuario();
        u.setPasswordHash(new BCryptPasswordEncoder().encode("correcta"));
        when(usuarioRepository.findByEmail("a@a.com")).thenReturn(Optional.of(u));
        assertThrows(RuntimeException.class,
                () -> usuarioService.login("a@a.com", "incorrecta"));
    }

    @Test
    void login_conEmailInexistente_lanzaError() {
        when(usuarioRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> usuarioService.login("noexiste@test.com", "cualquier"));
    }

    @Test
    void login_correcto_devuelveUsuario() {
        Usuario u = new Usuario();
        u.setEmail("naza@test.com");
        u.setPasswordHash(new BCryptPasswordEncoder().encode("1234"));
        when(usuarioRepository.findByEmail("naza@test.com")).thenReturn(Optional.of(u));
        Usuario resultado = usuarioService.login("naza@test.com", "1234");
        assertNotNull(resultado);
        assertEquals("naza@test.com", resultado.getEmail());
    }

    @Test
    void registrar_hashea_password_y_asigna_rol_cliente() {
        Usuario u = new Usuario();
        u.setEmail("nuevo@test.com");
        u.setPasswordHash("plain123");
        when(usuarioRepository.existsByEmail("nuevo@test.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));
        Usuario resultado = usuarioService.registrar(u);
        assertEquals("CLIENTE", resultado.getRol());
        assertNotEquals("plain123", resultado.getPasswordHash());
    }
}