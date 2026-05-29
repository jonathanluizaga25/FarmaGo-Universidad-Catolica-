package com.ucb.farmago.backend.repositories;
import com.ucb.farmago.backend.models.Familiar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FamiliarRepository extends JpaRepository<Familiar, Long> {
    List<Familiar> findByUsuarioId(Long usuarioId);
}