<<<<<<< HEAD

=======
>>>>>>> origin/main
package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.dto.DetallePedidoDTO;
import com.ucb.farmago.backend.dto.PedidoRepartidorDTO;
import com.ucb.farmago.backend.models.Entrega;
import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.repositories.DetallePedidoRepository;
import com.ucb.farmago.backend.repositories.EntregaRepository;
import com.ucb.farmago.backend.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import com.ucb.farmago.backend.dto.DetallePedidoDTO;
=======
import org.springframework.transaction.annotation.Transactional;

>>>>>>> origin/main
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepartidorService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private EntregaRepository entregaRepository;

<<<<<<< HEAD
    // Todos los pedidos asignados al repartidor
=======
    @Transactional(readOnly = true)
>>>>>>> origin/main
    public List<PedidoRepartidorDTO> obtenerPedidosAsignados(Long repartidorId) {
        List<Pedido> pedidos = pedidoRepository.findByRepartidorId(repartidorId);

        return pedidos.stream().map(pedido -> {
            List<DetallePedidoDTO> detalles = detallePedidoRepository
                    .findByPedidoId(pedido.getId())
                    .stream()
                    .map(DetallePedidoDTO::new)
                    .collect(Collectors.toList());

            List<Entrega> entregas = entregaRepository.findByPedidoId(pedido.getId());
            Entrega entrega = entregas.isEmpty() ? null : entregas.get(0);

            return new PedidoRepartidorDTO(pedido, entrega, detalles);
        }).collect(Collectors.toList());
    }

<<<<<<< HEAD
    // Pedidos asignados filtrados por estado (ej: solo EN_CAMINO)
=======
    @Transactional(readOnly = true)
>>>>>>> origin/main
    public List<PedidoRepartidorDTO> obtenerPedidosPorEstado(Long repartidorId, String estado) {
        List<Pedido> pedidos = pedidoRepository.findByRepartidorId(repartidorId)
                .stream()
                .filter(p -> p.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());

        return pedidos.stream().map(pedido -> {
            List<DetallePedidoDTO> detalles = detallePedidoRepository
                    .findByPedidoId(pedido.getId())
                    .stream()
                    .map(DetallePedidoDTO::new)
                    .collect(Collectors.toList());

            List<Entrega> entregas = entregaRepository.findByPedidoId(pedido.getId());
            Entrega entrega = entregas.isEmpty() ? null : entregas.get(0);

            return new PedidoRepartidorDTO(pedido, entrega, detalles);
        }).collect(Collectors.toList());
    }

<<<<<<< HEAD
    // Actualizar estado de la entrega (el repartidor marca en camino, entregado, etc.)
=======
    @Transactional
>>>>>>> origin/main
    public PedidoRepartidorDTO actualizarEstadoEntrega(Long entregaId, String estadoEntrega,
                                                        String observacion, Long repartidorId) {
        Entrega entrega = entregaRepository.findById(entregaId)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));

        if (!entrega.getRepartidor().getId().equals(repartidorId)) {
            throw new RuntimeException("No autorizado");
        }

        entrega.setEstado(estadoEntrega);

        if (observacion != null) {
            entrega.setObservacion(observacion);
        }

<<<<<<< HEAD
        if (estadoEntrega.equals("ENTREGADO")) {
            entrega.setEntregaExitosa(true);
            entrega.setFechaEntrega(java.time.LocalDateTime.now());
        } else if (estadoEntrega.equals("FALLIDO")) {
=======
        if (estadoEntrega.equalsIgnoreCase("ENTREGADO")) {
            entrega.setEntregaExitosa(true);
            entrega.setFechaEntrega(java.time.LocalDateTime.now());
        } else if (estadoEntrega.equalsIgnoreCase("FALLIDO")) {
>>>>>>> origin/main
            entrega.setEntregaExitosa(false);
            entrega.setFechaEntrega(java.time.LocalDateTime.now());
        }

        entregaRepository.save(entrega);

<<<<<<< HEAD
        // Sync estado del pedido también
        Pedido pedido = entrega.getPedido();
        if (estadoEntrega.equals("EN_CAMINO")) pedido.setEstado("EN_CAMINO");
        else if (estadoEntrega.equals("ENTREGADO")) pedido.setEstado("ENTREGADO");
        else if (estadoEntrega.equals("FALLIDO")) pedido.setEstado("FALLIDO");
=======
        Pedido pedido = entrega.getPedido();
        if (estadoEntrega.equalsIgnoreCase("EN_CAMINO")) pedido.setEstado("EN_CAMINO");
        else if (estadoEntrega.equalsIgnoreCase("ENTREGADO")) pedido.setEstado("ENTREGADO");
        else if (estadoEntrega.equalsIgnoreCase("FALLIDO")) pedido.setEstado("FALLIDO");
>>>>>>> origin/main
        pedidoRepository.save(pedido);

        List<DetallePedidoDTO> detalles = detallePedidoRepository
                .findByPedidoId(pedido.getId())
                .stream()
                .map(DetallePedidoDTO::new)
                .collect(Collectors.toList());

        return new PedidoRepartidorDTO(pedido, entrega, detalles);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/main
