package com.ucb.farmago.backend.repositories;

import com.ucb.farmago.backend.models.LogPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogPedidoRepository extends JpaRepository<LogPedido, Long> {
}