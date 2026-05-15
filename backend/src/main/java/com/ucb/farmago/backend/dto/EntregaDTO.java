package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Entrega;
import java.time.LocalDateTime;

public class EntregaDTO {

    private Long id;
    private PedidoDTO pedido;
    private UsuarioDTO repartidor;
    private String estado;
    private String observacion;
    private LocalDateTime fechaEntrega;
    private Boolean entregaExitosa;

    public EntregaDTO(Entrega entrega) {
        this.id = entrega.getId();
        this.pedido = entrega.getPedido() != null ? new PedidoDTO(entrega.getPedido()) : null;
        this.repartidor = entrega.getRepartidor() != null ? new UsuarioDTO(entrega.getRepartidor()) : null;
        this.estado = entrega.getEstado();
        this.observacion = entrega.getObservacion();
        this.fechaEntrega = entrega.getFechaEntrega();
        this.entregaExitosa = entrega.getEntregaExitosa();
    }

    public Long getId() { return id; }
    public PedidoDTO getPedido() { return pedido; }
    public UsuarioDTO getRepartidor() { return repartidor; }
    public String getEstado() { return estado; }
    public String getObservacion() { return observacion; }
    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public Boolean getEntregaExitosa() { return entregaExitosa; }
}
