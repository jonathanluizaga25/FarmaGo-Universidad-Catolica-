package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Lote;
import java.math.BigDecimal;
import java.util.Date;

public class LoteDTO {

    private Long id;
    private ProductoDTO producto;
    private Integer cantidad;
    private BigDecimal costoUnitario;
    private Date fechaEntrada;
    private Date fechaVencimiento;
    private String estado;

    public LoteDTO(Lote lote) {
        this.id = lote.getId();
        this.producto = lote.getProducto() != null ? new ProductoDTO(lote.getProducto()) : null;
        this.cantidad = lote.getCantidad();
        this.costoUnitario = lote.getCostoUnitario();
        this.fechaEntrada = lote.getFechaEntrada();
        this.fechaVencimiento = lote.getFechaVencimiento();
        this.estado = lote.getEstado();
    }

    public Long getId() { return id; }
    public ProductoDTO getProducto() { return producto; }
    public Integer getCantidad() { return cantidad; }
    public BigDecimal getCostoUnitario() { return costoUnitario; }
    public Date getFechaEntrada() { return fechaEntrada; }
    public Date getFechaVencimiento() { return fechaVencimiento; }
    public String getEstado() { return estado; }
}
