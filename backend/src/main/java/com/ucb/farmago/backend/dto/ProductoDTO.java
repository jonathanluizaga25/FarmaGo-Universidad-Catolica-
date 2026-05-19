package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Producto;
import java.math.BigDecimal;

public class ProductoDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagenUrl;
    private String categoria;
    private Integer stockMinimo;
    private ProveedorDTO laboratorio;
    private Integer stockActual;

    public Integer getStockActual() { 
        return stockActual; 
    }

    public ProductoDTO(Producto producto) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.descripcion = producto.getDescripcion();
        this.precio = producto.getPrecio();
        this.imagenUrl = producto.getImagenUrl();
        this.categoria = producto.getCategoria();
        this.stockMinimo = producto.getStockMinimo();
        this.stockActual = producto.getStockActual();
        this.laboratorio = producto.getLaboratorio() != null ? new ProveedorDTO(producto.getLaboratorio()) : null;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public String getImagenUrl() { return imagenUrl; }
    public String getCategoria() { return categoria; }
    public Integer getStockMinimo() { return stockMinimo; }
    public ProveedorDTO getLaboratorio() { return laboratorio; }
}
