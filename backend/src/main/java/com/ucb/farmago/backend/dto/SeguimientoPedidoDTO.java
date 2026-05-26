package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.models.Entrega;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SeguimientoPedidoDTO {

    private Long id;
    private String estado;
    private String tipoEntrega;
    private String metodoPago;
    private BigDecimal total;
    private BigDecimal costoEnvio;
    private LocalDateTime createdAt;
    private List<DetallePedidoDTO> productos;

    // Datos de la entrega (si existe)
    private String estadoEntrega;
    private String observacionEntrega;
    private LocalDateTime fechaEntrega;
    private Boolean entregaExitosa;
    private String nombreRepartidor;

    public SeguimientoPedidoDTO(Pedido pedido, Entrega entrega, List<DetallePedidoDTO> detalles) {
        this.id = pedido.getId();
        this.estado = pedido.getEstado();
        this.tipoEntrega = pedido.getTipoEntrega();
        this.metodoPago = pedido.getMetodoPago();
        this.total = pedido.getTotal();
        this.costoEnvio = pedido.getCostoEnvio();
        this.createdAt = pedido.getCreatedAt();
        this.productos = detalles;

        if (entrega != null) {
            this.estadoEntrega = entrega.getEstado();
            this.observacionEntrega = entrega.getObservacion();
            this.fechaEntrega = entrega.getFechaEntrega();
            this.entregaExitosa = entrega.getEntregaExitosa();
            if (entrega.getRepartidor() != null) {
                this.nombreRepartidor = entrega.getRepartidor().getNombre();
            }
        }
    }

    public Long getId() { return id; }
    public String getEstado() { return estado; }
    public String getTipoEntrega() { return tipoEntrega; }
    public String getMetodoPago() { return metodoPago; }
    public BigDecimal getTotal() { return total; }
    public BigDecimal getCostoEnvio() { return costoEnvio; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<DetallePedidoDTO> getProductos() { return productos; }
    public String getEstadoEntrega() { return estadoEntrega; }
    public String getObservacionEntrega() { return observacionEntrega; }
    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public Boolean getEntregaExitosa() { return entregaExitosa; }
    public String getNombreRepartidor() { return nombreRepartidor; }
}