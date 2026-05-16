package com.ucb.farmago.backend.repositories;

import com.ucb.farmago.backend.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByEstado(String estado);
    List<Pedido> findByRepartidorId(Long repartidorId);
}
