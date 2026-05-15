package com.ucb.farmago.backend.dto;

import com.ucb.farmago.backend.models.Usuario;
import java.time.LocalDateTime;

public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String email;
    private String direccion;
    private String telefono;
    private String rol;
    private LocalDateTime createdAt;

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.email = usuario.getEmail();
        this.direccion = usuario.getDireccion();
        this.telefono = usuario.getTelefono();
        this.rol = usuario.getRol();
        this.createdAt = usuario.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getRol() { return rol; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
