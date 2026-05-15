package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.models.Producto;
import com.ucb.farmago.backend.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listarTodos() {
        return productoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.crear(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.actualizar(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.ok("Producto eliminado");
    }

    @GetMapping("/categoria/{categoria}")
    public List<Producto> listarPorCategoria(@PathVariable String categoria) {
        return productoService.listarPorCategoria(categoria);
    }
}
