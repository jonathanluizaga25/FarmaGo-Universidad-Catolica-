package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Carrito;
import com.ucb.farmago.backend.models.CarritoItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CarritoDTO {

    private Long id;
    private Long clienteId;
    private String estado;
    private List<ItemDTO> items;
    private BigDecimal total;

    public CarritoDTO(Carrito carrito) {
        this.id = carrito.getId();
        this.clienteId = carrito.getCliente().getId();
        this.estado = carrito.getEstado();
        this.items = carrito.getItems().stream()
                .map(ItemDTO::new)
                .collect(Collectors.toList());
        this.total = this.items.stream()
                .map(i -> i.getSubtotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() { return id; }
    public Long getClienteId() { return clienteId; }
    public String getEstado() { return estado; }
    public List<ItemDTO> getItems() { return items; }
    public BigDecimal getTotal() { return total; }

    public static class ItemDTO {
        private Long id;
        private Long productoId;
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;

        public ItemDTO(CarritoItem item) {
            this.id = item.getId();
            this.productoId = item.getProducto().getId();
            this.nombreProducto = item.getProducto().getNombre();
            this.cantidad = item.getCantidad();
            this.precioUnitario = item.getPrecioUnitario();
            this.subtotal = item.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));
        }

        public Long getId() { return id; }
        public Long getProductoId() { return productoId; }
        public String getNombreProducto() { return nombreProducto; }
        public Integer getCantidad() { return cantidad; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public BigDecimal getSubtotal() { return subtotal; }
    }
}