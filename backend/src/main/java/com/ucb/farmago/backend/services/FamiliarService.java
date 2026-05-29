package com.ucb.farmago.backend.services;
import com.ucb.farmago.backend.models.Familiar;
import com.ucb.farmago.backend.models.Usuario;
import com.ucb.farmago.backend.repositories.FamiliarRepository;
import com.ucb.farmago.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class FamiliarService {
    @Autowired
    private FamiliarRepository familiarRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    public Familiar agregar(Long usuarioId, Familiar familiar) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        familiar.setUsuario(usuario);
        return familiarRepository.save(familiar);
    }
    public List<Familiar> listarPorUsuario(Long usuarioId) {
        return familiarRepository.findByUsuarioId(usuarioId);
    }
    public void eliminarDe(Long usuarioId, Long familiarId) {
        Familiar familiar = familiarRepository.findById(familiarId).orElseThrow(() -> new RuntimeException("Familiar no encontrado"));
        if (!familiar.getUsuario().getId().equals(usuarioId)) throw new RuntimeException("El familiar no pertenece a este usuario");
        familiarRepository.deleteById(familiarId);
    }
}