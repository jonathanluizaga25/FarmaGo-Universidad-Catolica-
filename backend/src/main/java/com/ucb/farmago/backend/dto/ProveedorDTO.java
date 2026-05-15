package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Proveedor;

public class ProveedorDTO {

    private Long id;
    private String nombre;
    private String contacto;
    private String telefono;
    private String laboratorio;

    public ProveedorDTO(Proveedor proveedor) {
        this.id = proveedor.getId();
        this.nombre = proveedor.getNombre();
        this.contacto = proveedor.getContacto();
        this.telefono = proveedor.getTelefono();
        this.laboratorio = proveedor.getLaboratorio();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getContacto() { return contacto; }
    public String getTelefono() { return telefono; }
    public String getLaboratorio() { return laboratorio; }
}
