package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Alerta;
import java.time.LocalDateTime;

public class AlertaDTO {

    private Long id;
    private String tipo;
    private ProductoDTO producto;
    private LoteDTO lote;
    private String mensaje;
    private Boolean leida;
    private LocalDateTime createdAt;

    public AlertaDTO(Alerta alerta) {
        this.id = alerta.getId();
        this.tipo = alerta.getTipo();
        this.producto = alerta.getProducto() != null ? new ProductoDTO(alerta.getProducto()) : null;
        this.lote = alerta.getLote() != null ? new LoteDTO(alerta.getLote()) : null;
        this.mensaje = alerta.getMensaje();
        this.leida = alerta.getLeida();
        this.createdAt = alerta.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public ProductoDTO getProducto() { return producto; }
    public LoteDTO getLote() { return lote; }
    public String getMensaje() { return mensaje; }
    public Boolean getLeida() { return leida; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
