package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.models.Entrega;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoRepartidorDTO {

    private Long pedidoId;
    private String estadoPedido;
    private String tipoEntrega;
    private String metodoPago;
    private BigDecimal total;
    private BigDecimal costoEnvio;
    private LocalDateTime createdAt;

    // Datos del cliente
    private String nombreCliente;
    private String telefonoCliente;
    private String direccionCliente;

    // Datos de la entrega
    private Long entregaId;
    private String estadoEntrega;
    private String observacion;
    private LocalDateTime fechaEntrega;
    private Boolean entregaExitosa;

    // Productos del pedido
    private List<DetallePedidoDTO> productos;

    public PedidoRepartidorDTO(Pedido pedido, Entrega entrega, List<DetallePedidoDTO> detalles) {
        this.pedidoId = pedido.getId();
        this.estadoPedido = pedido.getEstado();
        this.tipoEntrega = pedido.getTipoEntrega();
        this.metodoPago = pedido.getMetodoPago();
        this.total = pedido.getTotal();
        this.costoEnvio = pedido.getCostoEnvio();
        this.createdAt = pedido.getCreatedAt();
        this.productos = detalles;

        if (pedido.getCliente() != null) {
            this.nombreCliente = pedido.getCliente().getNombre();
            this.telefonoCliente = pedido.getCliente().getTelefono();
            this.direccionCliente = pedido.getCliente().getDireccion();
        }

        if (entrega != null) {
            this.entregaId = entrega.getId();
            this.estadoEntrega = entrega.getEstado();
            this.observacion = entrega.getObservacion();
            this.fechaEntrega = entrega.getFechaEntrega();
            this.entregaExitosa = entrega.getEntregaExitosa();
        }
    }

    public Long getPedidoId() { return pedidoId; }
    public String getEstadoPedido() { return estadoPedido; }
    public String getTipoEntrega() { return tipoEntrega; }
    public String getMetodoPago() { return metodoPago; }
    public BigDecimal getTotal() { return total; }
    public BigDecimal getCostoEnvio() { return costoEnvio; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getNombreCliente() { return nombreCliente; }
    public String getTelefonoCliente() { return telefonoCliente; }
    public String getDireccionCliente() { return direccionCliente; }
    public Long getEntregaId() { return entregaId; }
    public String getEstadoEntrega() { return estadoEntrega; }
    public String getObservacion() { return observacion; }
    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public Boolean getEntregaExitosa() { return entregaExitosa; }
    public List<DetallePedidoDTO> getProductos() { return productos; }
}