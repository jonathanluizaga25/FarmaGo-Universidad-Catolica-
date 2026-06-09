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
<<<<<<< HEAD
=======
    private String tipo;
>>>>>>> origin/main
    private Integer stockMinimo;
    private ProveedorDTO laboratorio;
    private Integer stockActual;

<<<<<<< HEAD
    public Integer getStockActual() { 
        return stockActual; 
    }

=======
>>>>>>> origin/main
    public ProductoDTO(Producto producto) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.descripcion = producto.getDescripcion();
        this.precio = producto.getPrecio();
        this.imagenUrl = producto.getImagenUrl();
        this.categoria = producto.getCategoria();
<<<<<<< HEAD
=======
        this.tipo = producto.getTipo();
>>>>>>> origin/main
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
<<<<<<< HEAD
    public Integer getStockMinimo() { return stockMinimo; }
    public ProveedorDTO getLaboratorio() { return laboratorio; }
}
=======
    public String getTipo() { return tipo; }
    public Integer getStockMinimo() { return stockMinimo; }
    public Integer getStockActual() { return stockActual; }
    public ProveedorDTO getLaboratorio() { return laboratorio; }
}
>>>>>>> origin/main
