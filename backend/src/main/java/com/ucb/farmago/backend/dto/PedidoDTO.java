package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Pedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoDTO {

    private Long id;
    private UsuarioDTO cliente;
    private UsuarioDTO repartidor;
    private BigDecimal total;
    private String estado;
    private String tipoEntrega;
    private String metodoPago;
    private BigDecimal costoEnvio;
    private LocalDateTime createdAt;

    public PedidoDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.cliente = pedido.getCliente() != null ? new UsuarioDTO(pedido.getCliente()) : null;
        this.repartidor = pedido.getRepartidor() != null ? new UsuarioDTO(pedido.getRepartidor()) : null;
        this.total = pedido.getTotal();
        this.estado = pedido.getEstado();
        this.tipoEntrega = pedido.getTipoEntrega();
        this.metodoPago = pedido.getMetodoPago();
        this.costoEnvio = pedido.getCostoEnvio();
        this.createdAt = pedido.getCreatedAt();
    }

    public Long getId() { return id; }
    public UsuarioDTO getCliente() { return cliente; }
    public UsuarioDTO getRepartidor() { return repartidor; }
    public BigDecimal getTotal() { return total; }
    public String getEstado() { return estado; }
    public String getTipoEntrega() { return tipoEntrega; }
    public String getMetodoPago() { return metodoPago; }
    public BigDecimal getCostoEnvio() { return costoEnvio; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
