package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.dto.FacturaValidacionDTO;
import com.ucb.farmago.backend.dto.HistorialPedidoDTO;
import com.ucb.farmago.backend.dto.ResultadoValidacionDTO;
import com.ucb.farmago.backend.models.Alerta;
import com.ucb.farmago.backend.models.Carrito;
import com.ucb.farmago.backend.models.DetallePedido;
import com.ucb.farmago.backend.models.Entrega;
import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.models.Usuario;
import com.ucb.farmago.backend.repositories.AlertaRepository;
import com.ucb.farmago.backend.repositories.CarritoRepository;
import com.ucb.farmago.backend.repositories.DetallePedidoRepository;
import com.ucb.farmago.backend.repositories.EntregaRepository;
import com.ucb.farmago.backend.repositories.PedidoRepository;
import com.ucb.farmago.backend.repositories.ProductoRepository;
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
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private AlertaRepository alertaRepository;

    @Autowired
    private EntregaRepository entregaRepository;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    @Transactional
    public Pedido crear(Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            throw new RuntimeException("Se requiere un cliente para crear el pedido");
        }
        Usuario cliente = usuarioRepository.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        pedido.setCliente(cliente);
        pedido.setEstado("PENDIENTE");

        Carrito carrito = carritoRepository.findByClienteIdAndEstado(cliente.getId(), "ACTIVO")
                .orElseThrow(() -> new RuntimeException("El carrito está vacío"));

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        Pedido guardado = pedidoRepository.save(pedido);

        for (var item : carrito.getItems()) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(guardado);
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detallePedidoRepository.save(detalle);

            var producto = productoRepository.findByIdForUpdate(item.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            if (producto.getStockActual() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para " + producto.getNombre() +
                    " (disponible: " + producto.getStockActual() + ", pedido: " + item.getCantidad() + ")");
            }
            producto.setStockActual(producto.getStockActual() - item.getCantidad());
            productoRepository.save(producto);
        }

        // BUG FIX PRO: Creamos la entrega si es a domicilio, validando nulos y sin usar "=="
        if (pedido.getTipoEntrega() != null && "DOMICILIO".equalsIgnoreCase(pedido.getTipoEntrega().trim())) {
            Entrega entrega = new Entrega();
            entrega.setPedido(guardado);
            entrega.setEstado("PENDIENTE_ASIGNACION");
            entregaRepository.save(entrega);
        }

        carrito.getItems().clear();
        carritoRepository.save(carrito);

        // Alerta al administrador cuando se crea un nuevo pedido
        Alerta alerta = new Alerta();
        alerta.setTipo("NUEVO_PEDIDO");
        alerta.setMensaje("Nuevo pedido creado - ID: " + guardado.getId() +
                " | Cliente: " + (guardado.getCliente() != null ? guardado.getCliente().getNombre() : "Desconocido") +
                " | Total: " + guardado.getTotal() + " Bs");
        alerta.setLeida(Boolean.FALSE);
        alertaRepository.save(alerta);

        return pedidoRepository.findById(guardado.getId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
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

    @Transactional
    public Pedido cancelar(Long id) {
        Pedido pedido = obtenerPorId(id);
// BUG FIX: Validamos nulos y espacios ocultos con .trim()
        if (pedido.getEstado() == null || !pedido.getEstado().trim().equalsIgnoreCase("PENDIENTE")) {
            throw new RuntimeException("Solo se pueden cancelar pedidos en estado PENDIENTE");
        }
        
        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(id);
        for (DetallePedido detalle : detalles) {
            var producto = detalle.getProducto();
            producto.setStockActual(producto.getStockActual() + detalle.getCantidad());
            productoRepository.save(producto);
        }
        
        pedido.setEstado("CANCELADO");
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public ResultadoValidacionDTO validarFacturaVsPedido(FacturaValidacionDTO facturaDTO) {
        Pedido pedido = obtenerPorId(facturaDTO.getPedidoId());
        List<DetallePedido> detallesOriginales = detallePedidoRepository.findByPedidoId(pedido.getId());

        if (facturaDTO.getItems() == null || facturaDTO.getItems().size() != detallesOriginales.size()) {
            pedido.setEstado("RECHAZADO_DEVOLUCION");
            pedidoRepository.save(pedido);
            return new ResultadoValidacionDTO("RECHAZADO_DEVOLUCION",
                    "Discrepancia: La cantidad de ítems en la factura no coincide.");
        }

        for (DetallePedido detalleOriginal : detallesOriginales) {
            Long prodId = detalleOriginal.getProducto().getId();

            FacturaValidacionDTO.ItemFacturaDTO itemFactura = facturaDTO.getItems().stream()
                    .filter(item -> item.getProductoId().equals(prodId))
                    .findFirst()
                    .orElse(null);

            if (itemFactura == null) {
                pedido.setEstado("RECHAZADO_DEVOLUCION");
                pedidoRepository.save(pedido);
                return new ResultadoValidacionDTO("RECHAZADO_DEVOLUCION",
                        "Discrepancia: El producto ID " + prodId + " no figura en la factura.");
            }

            if (!itemFactura.getCantidad().equals(detalleOriginal.getCantidad())) {
                pedido.setEstado("RECHAZADO_DEVOLUCION");
                pedidoRepository.save(pedido);
                return new ResultadoValidacionDTO("RECHAZADO_DEVOLUCION",
                        "Discrepancia en Producto ID " + prodId + " por cantidad.");
            }

            if (itemFactura.getPrecioUnitario().compareTo(detalleOriginal.getPrecioUnitario()) != 0) {
                pedido.setEstado("RECHAZADO_DEVOLUCION");
                pedidoRepository.save(pedido);
                return new ResultadoValidacionDTO("RECHAZADO_DEVOLUCION",
                        "Discrepancia en Producto ID " + prodId + " por precio.");
            }
        }

        pedido.setEstado("RECIBIDO");
        pedidoRepository.save(pedido);
        return new ResultadoValidacionDTO("RECIBIDO", "Validación exitosa. La factura coincide con el pedido.");
    }

    @Transactional(readOnly = true)
    public List<HistorialPedidoDTO> obtenerHistorialCliente(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
        return pedidos.stream().map(pedido -> {
            List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedido.getId());
            return new HistorialPedidoDTO(pedido, detalles);
        }).collect(Collectors.toList());
    }
}
