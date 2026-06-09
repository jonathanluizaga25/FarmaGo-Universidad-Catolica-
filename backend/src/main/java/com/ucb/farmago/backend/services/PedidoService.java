package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.models.LogPedido;
import com.ucb.farmago.backend.repositories.LogPedidoRepository;
import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private LogPedidoRepository logPedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido crear(Pedido pedido) {
        pedido.setCreatedAt(java.time.LocalDateTime.now());
        pedido.setEstado("Pendiente");
        
        Double dolares = pedido.getValorDolares();
        
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        if (dolares != null) {
            LogPedido log = new LogPedido(pedidoGuardado.getId(), dolares);
            logPedidoRepository.save(log);
            
            pedidoGuardado.setValorDolares(dolares);
            pedidoGuardado.setValorBolivianos(Math.round((dolares * 6.96) * 100.0) / 100.0);
        }

        return pedidoGuardado;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
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

    public BigDecimal calcularCostoEnvio(String direccion) {
        if (direccion == null || direccion.isEmpty()) {
            return new BigDecimal("15");
        }
        String dir = direccion.toLowerCase();
        
        if (dir.contains("centro") || dir.contains("central")) {
            return BigDecimal.ZERO;
        }
        
        if (dir.contains("norte") || dir.contains("sur")) {
            return new BigDecimal("10");
        }
        
        return new BigDecimal("15");
    }
}