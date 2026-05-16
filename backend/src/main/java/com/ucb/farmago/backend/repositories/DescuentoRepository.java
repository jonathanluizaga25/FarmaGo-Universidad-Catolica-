package com.ucb.farmago.backend.repositories;

import com.ucb.farmago.backend.models.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Long> {
    List<Descuento> findByProductoId(Long productoId);
    List<Descuento> findByActivo(Boolean activo);
}
