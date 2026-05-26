package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.repositories.DetallePedidoRepository;
import com.ucb.farmago.backend.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

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

    // HU-14: Validación de factura vs. pedido al recibir mercadería
    public com.ucb.farmago.backend.dto.ResultadoValidacionDTO validarFacturaVsPedido(com.ucb.farmago.backend.dto.FacturaValidacionDTO facturaDTO) {
        Pedido pedido = obtenerPorId(facturaDTO.getPedidoId());
        List<com.ucb.farmago.backend.models.DetallePedido> detallesOriginales = detallePedidoRepository.findByPedidoId(pedido.getId());

        // Regla 1: Validar cantidad de elementos diferentes
        if (facturaDTO.getItems() == null || facturaDTO.getItems().size() != detallesOriginales.size()) {
            pedido.setEstado("RECHAZADO_DEVOLUCION");
            pedidoRepository.save(pedido);
            return new com.ucb.farmago.backend.dto.ResultadoValidacionDTO("RECHAZADO_DEVOLUCION",
                    "Discrepancia detectada: La cantidad de ítems en la factura (" +
                            (facturaDTO.getItems() != null ? facturaDTO.getItems().size() : 0) +
                            ") no coincide con el pedido original (" + detallesOriginales.size() + ").");
        }

        // Mapear el detalle original para búsquedas rápidas
        for (com.ucb.farmago.backend.models.DetallePedido detalleOriginal : detallesOriginales) {
            Long prodId = detalleOriginal.getProducto().getId();

            // Buscar el ítem correspondiente en la factura recibida
            com.ucb.farmago.backend.dto.FacturaValidacionDTO.ItemFacturaDTO itemFactura = facturaDTO.getItems().stream()
                    .filter(item -> item.getProductoId().equals(prodId))
                    .findFirst()
                    .orElse(null);

            // Regla 2: Producto faltante
            if (itemFactura == null) {
                pedido.setEstado("RECHAZADO_DEVOLUCION");
                pedidoRepository.save(pedido);
                return new com.ucb.farmago.backend.dto.ResultadoValidacionDTO("RECHAZADO_DEVOLUCION",
                        "Discrepancia detectada: El producto ID " + prodId + " requerido en el pedido no figura en la factura.");
            }

            // Regla 3: Validar cantidad del producto
            if (!itemFactura.getCantidad().equals(detalleOriginal.getCantidad())) {
                pedido.setEstado("RECHAZADO_DEVOLUCION");
                pedidoRepository.save(pedido);
                return new com.ucb.farmago.backend.dto.ResultadoValidacionDTO("RECHAZADO_DEVOLUCION",
                        "Discrepancia detectada en Producto ID " + prodId + ": Cantidad facturada (" +
                                itemFactura.getCantidad() + ") difiere de la pedida (" + detalleOriginal.getCantidad() + ").");
            }

            // Regla 4: Validar precio unitario
            if (itemFactura.getPrecioUnitario().compareTo(detalleOriginal.getPrecioUnitario()) != 0) {
                pedido.setEstado("RECHAZADO_DEVOLUCION");
                pedidoRepository.save(pedido);
                return new com.ucb.farmago.backend.dto.ResultadoValidacionDTO("RECHAZADO_DEVOLUCION",
                        "Discrepancia detectada en Producto ID " + prodId + ": Precio facturado (" +
                                itemFactura.getPrecioUnitario() + ") difiere del acordado (" + detalleOriginal.getPrecioUnitario() + ").");
            }
        }

        // Si todo coincide perfectamente, se registra el ingreso cambiando el estado a RECIBIDO
        pedido.setEstado("RECIBIDO");
        pedidoRepository.save(pedido);
        return new com.ucb.farmago.backend.dto.ResultadoValidacionDTO("RECIBIDO", "Validación exitosa. La factura coincide plenamente con el pedido.");
    }
}
