package com.ucb.farmago.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "log_pedido")
public class LogPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pedidoId;

    private Double valorDolares;

    public LogPedido() {
    }

    public LogPedido(Long pedidoId, Double valorDolares) {
        this.pedidoId = pedidoId;
        this.valorDolares = valorDolares;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public Double getValorDolares() { return valorDolares; }
    public void setValorDolares(Double valorDolares) { this.valorDolares = valorDolares; }
}