package com.ucb.farmago.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class FacturaValidacionDTO {

    private Long pedidoId;
    private List<ItemFacturaDTO> items;

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public List<ItemFacturaDTO> getItems() { return items; }
    public void setItems(List<ItemFacturaDTO> items) { this.items = items; }

    public static class ItemFacturaDTO {
        private Long productoId;
        private Integer cantidad;
        private BigDecimal precioUnitario;

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    }
}
