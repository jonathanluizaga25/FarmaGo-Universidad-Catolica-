package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.dto.HistorialDTO;
import com.ucb.farmago.backend.models.DetallePedido;
import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.repositories.DetallePedidoRepository;
import com.ucb.farmago.backend.repositories.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistorialService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public HistorialService(PedidoRepository pedidoRepository, DetallePedidoRepository detallePedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
    }

    // Example method to use the repositories and avoid unused field warning
    public List<HistorialDTO> getHistorialCliente(Long clienteId) {

    List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);

    System.out.println("=== HISTORIAL DEBUG ===");
    System.out.println("ClienteId buscado: " + clienteId);
    System.out.println("Pedidos encontrados: " + pedidos.size());

    List<HistorialDTO> historial = new ArrayList<>();

    for (Pedido pedido : pedidos) {

        System.out.println("Procesando pedido id: " + pedido.getId());

        List<DetallePedido> detalles = detallePedidoRepository
                .findByPedidoId(pedido.getId());

        System.out.println("Detalles encontrados: " + detalles.size());

        List<HistorialDTO.ItemHistorialDTO> items = new ArrayList<>();

        for (DetallePedido detalle : detalles) {
            System.out.println("Producto: " + detalle.getProducto().getNombre());
            items.add(new HistorialDTO.ItemHistorialDTO(
                    detalle.getProducto().getNombre(),
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario()
            ));
        }

        HistorialDTO dto = new HistorialDTO(
                pedido.getId(),
                pedido.getCreatedAt(),
                pedido.getTotal(),
                pedido.getEstado(),
                pedido.getMetodoPago(),
                items
        );

        System.out.println("DTO creado: " + dto.getPedidoId());
        historial.add(dto);
    }

    System.out.println("Total historial: " + historial.size());
    return historial;
}
    
}
