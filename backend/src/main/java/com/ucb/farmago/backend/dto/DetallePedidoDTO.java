package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.DetallePedido;
<<<<<<< HEAD

public class DetallePedidoDTO {
    
    private Long productoId;
    private Integer cantidad;

    public DetallePedidoDTO() {
    }

    public DetallePedidoDTO(DetallePedido detalle) {
        this.productoId = detalle.getProducto().getId();
        this.cantidad = detalle.getCantidad();
    }

    public DetallePedidoDTO(Long productoId, Integer cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
=======
import java.math.BigDecimal;

public class DetallePedidoDTO {

    private Long id;
    private ProductoDTO producto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public DetallePedidoDTO(DetallePedido detalle) {
        this.id = detalle.getId();
        this.producto = detalle.getProducto() != null ? new ProductoDTO(detalle.getProducto()) : null;
        this.cantidad = detalle.getCantidad();
        this.precioUnitario = detalle.getPrecioUnitario();
        this.subtotal = detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()));
    }

    public Long getId() { return id; }
    public ProductoDTO getProducto() { return producto; }
    public Integer getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
}
>>>>>>> origin/main
