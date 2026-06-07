package com.ucb.farmago.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class HistorialDTO {

    @JsonProperty("pedidoId")
    private Long pedidoId;

    @JsonProperty("fecha")
    private LocalDateTime fecha;

    @JsonProperty("total")
    private BigDecimal total;

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("metodoPago")
    private String metodoPago;

    @JsonProperty("productos")
    private List<ItemHistorialDTO> productos;

    public static class ItemHistorialDTO {

        @JsonProperty("nombreProducto")
        private String nombreProducto;

        @JsonProperty("cantidad")
        private Integer cantidad;

        @JsonProperty("precioUnitario")
        private BigDecimal precioUnitario;

        @JsonProperty("subtotal")
        private BigDecimal subtotal;

        public ItemHistorialDTO(String nombreProducto, Integer cantidad, BigDecimal precioUnitario) {
            this.nombreProducto = nombreProducto;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }

        public String getNombreProducto() { return nombreProducto; }
        public Integer getCantidad() { return cantidad; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public BigDecimal getSubtotal() { return subtotal; }
    }

    public HistorialDTO(Long pedidoId, LocalDateTime fecha, BigDecimal total,
                        String estado, String metodoPago, List<ItemHistorialDTO> productos) {
        this.pedidoId = pedidoId;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.metodoPago = metodoPago;
        this.productos = productos;
    }

    public Long getPedidoId() { return pedidoId; }
    public LocalDateTime getFecha() { return fecha; }
    public BigDecimal getTotal() { return total; }
    public String getEstado() { return estado; }
    public String getMetodoPago() { return metodoPago; }
    public List<ItemHistorialDTO> getProductos() { return productos; }
}