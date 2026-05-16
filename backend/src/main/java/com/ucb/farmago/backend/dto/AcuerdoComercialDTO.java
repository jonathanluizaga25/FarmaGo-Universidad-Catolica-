package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.AcuerdoComercial;
import java.time.LocalDate;

public class AcuerdoComercialDTO {

    private Long id;
    private ProveedorDTO proveedor;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean activo;

    public AcuerdoComercialDTO(AcuerdoComercial acuerdo) {
        this.id = acuerdo.getId();
        this.proveedor = acuerdo.getProveedor() != null ? new ProveedorDTO(acuerdo.getProveedor()) : null;
        this.descripcion = acuerdo.getDescripcion();
        this.fechaInicio = acuerdo.getFechaInicio();
        this.fechaFin = acuerdo.getFechaFin();
        this.activo = acuerdo.getActivo();
    }

    public Long getId() { return id; }
    public ProveedorDTO getProveedor() { return proveedor; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public Boolean getActivo() { return activo; }
}
