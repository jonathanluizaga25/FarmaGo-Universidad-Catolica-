package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.models.DetallePedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class HistorialPedidoDTO {

    private Long id;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado;
    private List<ProductoHistorialDTO> productos;

    public HistorialPedidoDTO(Pedido pedido, List<DetallePedido> detalles) {
        this.id = pedido.getId();
        this.fecha = pedido.getCreatedAt();
        this.total = pedido.getTotal();
        this.estado = pedido.getEstado();
        this.productos = detalles.stream()
                .map(ProductoHistorialDTO::new)
                .collect(Collectors.toList());
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<ProductoHistorialDTO> getProductos() { return productos; }
    public void setProductos(List<ProductoHistorialDTO> productos) { this.productos = productos; }

    // Sub-DTO específico para simplificar los datos requeridos por el frontend
    public static class ProductoHistorialDTO {
        private Long productoId;
        private String nombre;
        private Integer cantidad;
        private BigDecimal precioUnitario;

        public ProductoHistorialDTO(DetallePedido detalle) {
            this.productoId = detalle.getProducto().getId();
            this.nombre = detalle.getProducto().getNombre();
            this.cantidad = detalle.getCantidad();
            this.precioUnitario = detalle.getPrecioUnitario();
        }

        // Getters y Setters
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    }
}