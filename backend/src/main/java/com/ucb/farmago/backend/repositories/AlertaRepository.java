package com.ucb.farmago.backend.repositories;

import com.ucb.farmago.backend.models.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByLeida(Boolean leida);
    List<Alerta> findByProductoId(Long productoId);
}
