package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.dto.DetallePedidoDTO;
import com.ucb.farmago.backend.dto.SeguimientoPedidoDTO;
import com.ucb.farmago.backend.models.Entrega;
import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.repositories.DetallePedidoRepository;
import com.ucb.farmago.backend.repositories.EntregaRepository;
import com.ucb.farmago.backend.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeguimientoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private EntregaRepository entregaRepository;

    // Todos los pedidos de un cliente con su estado de entrega
    public List<SeguimientoPedidoDTO> obtenerPedidosCliente(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);

        return pedidos.stream().map(pedido -> {
            List<DetallePedidoDTO> detalles = detallePedidoRepository
                    .findByPedidoId(pedido.getId())
                    .stream()
                    .map(DetallePedidoDTO::new)
                    .collect(Collectors.toList());

            List<Entrega> entregas = entregaRepository.findByPedidoId(pedido.getId());
            Entrega entrega = entregas.isEmpty() ? null : entregas.get(0);

            return new SeguimientoPedidoDTO(pedido, entrega, detalles);
        }).collect(Collectors.toList());
    }

    // Un pedido específico con detalle completo
    public SeguimientoPedidoDTO obtenerDetallePedido(Long pedidoId, Long clienteId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getCliente().getId().equals(clienteId)) {
            throw new RuntimeException("No autorizado");
        }

        List<DetallePedidoDTO> detalles = detallePedidoRepository
                .findByPedidoId(pedidoId)
                .stream()
                .map(DetallePedidoDTO::new)
                .collect(Collectors.toList());

        List<Entrega> entregas = entregaRepository.findByPedidoId(pedidoId);
        Entrega entrega = entregas.isEmpty() ? null : entregas.get(0);

        return new SeguimientoPedidoDTO(pedido, entrega, detalles);
    }
}