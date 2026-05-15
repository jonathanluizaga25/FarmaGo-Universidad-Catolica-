package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Descuento;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DescuentoDTO {

    private Long id;
    private ProveedorDTO proveedor;
    private ProductoDTO producto;
    private BigDecimal porcentaje;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean activo;

    public DescuentoDTO(Descuento descuento) {
        this.id = descuento.getId();
        this.proveedor = descuento.getProveedor() != null ? new ProveedorDTO(descuento.getProveedor()) : null;
        this.producto = descuento.getProducto() != null ? new ProductoDTO(descuento.getProducto()) : null;
        this.porcentaje = descuento.getPorcentaje();
        this.fechaInicio = descuento.getFechaInicio();
        this.fechaFin = descuento.getFechaFin();
        this.activo = descuento.getActivo();
    }

    public Long getId() { return id; }
    public ProveedorDTO getProveedor() { return proveedor; }
    public ProductoDTO getProducto() { return producto; }
    public BigDecimal getPorcentaje() { return porcentaje; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public Boolean getActivo() { return activo; }
}
