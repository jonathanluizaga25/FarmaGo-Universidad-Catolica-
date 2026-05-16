package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Descuento;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DescuentoDTO {

    private Long id;
    private AcuerdoComercialDTO acuerdo;
    private ProductoDTO producto;
    private BigDecimal porcentaje;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean activo;

    public DescuentoDTO(Descuento descuento) {
        this.id = descuento.getId();
        this.acuerdo = descuento.getAcuerdo() != null ? new AcuerdoComercialDTO(descuento.getAcuerdo()) : null;
        this.producto = descuento.getProducto() != null ? new ProductoDTO(descuento.getProducto()) : null;
        this.porcentaje = descuento.getPorcentaje();
        this.fechaInicio = descuento.getFechaInicio();
        this.fechaFin = descuento.getFechaFin();
        this.activo = descuento.getActivo();
    }

    public Long getId() { return id; }
    public AcuerdoComercialDTO getAcuerdo() { return acuerdo; }
    public ProductoDTO getProducto() { return producto; }
    public BigDecimal getPorcentaje() { return porcentaje; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public Boolean getActivo() { return activo; }
}