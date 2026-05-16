package com.ucb.farmago.backend.repositories;

import com.ucb.farmago.backend.models.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {
    List<Lote> findByProductoId(Long productoId);
    List<Lote> findByEstado(String estado);
}
