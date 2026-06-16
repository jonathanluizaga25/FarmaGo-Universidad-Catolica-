package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.dto.HistorialPedidoDTO;
import com.ucb.farmago.backend.models.DetallePedido;
import com.ucb.farmago.backend.models.Entrega;
import com.ucb.farmago.backend.repositories.EntregaRepository;
import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.models.Usuario;
import com.ucb.farmago.backend.repositories.DetallePedidoRepository;
import com.ucb.farmago.backend.repositories.PedidoRepository;
import com.ucb.farmago.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntregaRepository entregaRepository;

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

    // HU-A1: Asignar un repartidor a un pedido PENDIENTE desde el panel de supervision
    @Transactional
    public Pedido asignarRepartidor(Long pedidoId, Long repartidorId) {
        Pedido pedido = obtenerPorId(pedidoId);

        if (pedido.getEstado() == null || !pedido.getEstado().trim().equalsIgnoreCase("PENDIENTE")) {
            throw new RuntimeException("Solo se pueden asignar pedidos en estado PENDIENTE");
        }

        Usuario repartidor = usuarioRepository.findById(repartidorId)
                .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));

        if (repartidor.getRol() == null || !repartidor.getRol().trim().equalsIgnoreCase("REPARTIDOR")) {
            throw new RuntimeException("El usuario seleccionado no tiene rol REPARTIDOR");
        }

        pedido.setRepartidor(repartidor);
        pedido.setEstado("ASIGNADO");
        pedidoRepository.save(pedido);

        // Si el pedido es a domicilio ya tiene una Entrega creada (PENDIENTE_ASIGNACION),
        // le asignamos el repartidor y actualizamos su estado.
        List<Entrega> entregas = entregaRepository.findByPedidoId(pedido.getId());
        if (!entregas.isEmpty()) {
            Entrega entrega = entregas.get(0);
            entrega.setRepartidor(repartidor);
            entrega.setEstado("ASIGNADA");
            entregaRepository.save(entrega);
        }

        return pedido;
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
        if (dir.contains("centro") || dir.contains("central")) {
            return BigDecimal.ZERO;
        }
        if (dir.contains("norte") || dir.contains("sur")) {
            return new BigDecimal("10");
        }
        return new BigDecimal("15");
    }

    public List<HistorialPedidoDTO> obtenerHistorialCliente(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
        return pedidos.stream().map(pedido -> {
            List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedido.getId());
            return new HistorialPedidoDTO(pedido, detalles);
        }).collect(Collectors.toList());
    }

    // ==========================================
    // Anti-IDOR: verifica que el clienteId pertenece al email del token
    // Llamado desde PedidoController.verificarAccesoCliente()
    // ==========================================
    public boolean perteneceAlCliente(Long clienteId, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
        return usuario.getId().equals(clienteId);
    }
}
