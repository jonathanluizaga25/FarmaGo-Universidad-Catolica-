package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.dto.HistorialPedidoDTO;
import com.ucb.farmago.backend.models.DetallePedido;
import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.repositories.DetallePedidoRepository;
import com.ucb.farmago.backend.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

   public Pedido crear(Pedido pedido) {
    
    pedido.setEstado("Pendiente");
    return pedidoRepository.save(pedido);
    }
    public Pedido actualizarEstado(Long id, String estado) {
        Pedido pedido = obtenerPorId(id);
        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public List<Pedido> listarPorRepartidor(Long repartidorId) {
        return pedidoRepository.findByRepartidorId(repartidorId);
    }

    public List<Pedido> listarPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }

    // HU-10: Calcula el costo de envio segun la direccion del cliente
    public BigDecimal calcularCostoEnvio(String direccion) {
        if (direccion == null || direccion.isEmpty()) {
            return new BigDecimal("15");
        }
        String dir = direccion.toLowerCase();
        // Zona central: envio gratuito
        if (dir.contains("centro") || dir.contains("central")) {
            return BigDecimal.ZERO;
        }
        // Zona norte o sur: costo medio
        if (dir.contains("norte") || dir.contains("sur")) {
            return new BigDecimal("10");
        }
        // Zona lejana: costo mayor
        return new BigDecimal("15");
    }

    public List<HistorialPedidoDTO> obtenerHistorialCliente(Long clienteId) {
        // 1. Buscamos todos los pedidos del cliente usando el método existente del repositorio
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);

        // 2. Mapeamos cada pedido extrayendo sus respectivos productos y convirtiéndolo al DTO detallado
        return pedidos.stream().map(pedido -> {
            List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedido.getId());
            return new HistorialPedidoDTO(pedido, detalles);
        }).collect(Collectors.toList());
    }
}
