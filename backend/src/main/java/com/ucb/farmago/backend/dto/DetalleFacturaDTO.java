package com.ucb.farmago.backend.dto;

import java.math.BigDecimal;

public class DetalleFacturaDTO {
    private Long productoId;
    private Integer cantidadRecibida;
    private BigDecimal precioUnitarioFactura;

    public DetalleFacturaDTO() {}

    // Getters y Setters
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Integer getCantidadRecibida() { return cantidadRecibida; }
    public void setCantidadRecibida(Integer cantidadRecibida) { this.cantidadRecibida = cantidadRecibida; }

    public BigDecimal getPrecioUnitarioFactura() { return precioUnitarioFactura; }
    public void setPrecioUnitarioFactura(BigDecimal precioUnitarioFactura) { this.precioUnitarioFactura = precioUnitarioFactura; }
}