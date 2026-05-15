package com.ucb.farmago.backend.repositories;

import com.ucb.farmago.backend.models.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {
    List<Entrega> findByRepartidorId(Long repartidorId);
    List<Entrega> findByPedidoId(Long pedidoId);
}
