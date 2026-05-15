package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.models.Proveedor;
import com.ucb.farmago.backend.services.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public List<Proveedor> listarTodos() {
        return proveedorService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(proveedorService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Proveedor> crear(@RequestBody Proveedor proveedor) {
        return ResponseEntity.ok(proveedorService.crear(proveedor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        return ResponseEntity.ok(proveedorService.actualizar(id, proveedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        proveedorService.eliminar(id);
        return ResponseEntity.ok("Proveedor eliminado");
    }
}
