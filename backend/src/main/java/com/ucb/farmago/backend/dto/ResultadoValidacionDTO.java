package com.ucb.farmago.backend.dto;

public class ResultadoValidacionDTO {

    private String estado;
    private String observacion;

    public ResultadoValidacionDTO(String estado, String observacion) {
        this.estado = estado;
        this.observacion = observacion;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
