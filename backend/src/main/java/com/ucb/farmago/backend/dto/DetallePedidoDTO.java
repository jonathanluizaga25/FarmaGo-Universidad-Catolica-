package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.DetallePedido;

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