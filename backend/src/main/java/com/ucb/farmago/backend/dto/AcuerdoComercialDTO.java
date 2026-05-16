package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.AcuerdoComercial;

public class AcuerdoComercialDTO {

    private Long id;
    private ProveedorDTO proveedor;
    private ProductoDTO producto;

    public AcuerdoComercialDTO(AcuerdoComercial acuerdo) {
        this.id = acuerdo.getId();
        this.proveedor = acuerdo.getProveedor() != null ? new ProveedorDTO(acuerdo.getProveedor()) : null;
        this.producto = acuerdo.getProducto() != null ? new ProductoDTO(acuerdo.getProducto()) : null;
    }

    public Long getId() { return id; }
    public ProveedorDTO getProveedor() { return proveedor; }
    public ProductoDTO getProducto() { return producto; }
}