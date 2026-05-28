package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.dto.ProductoDTO;
import com.ucb.farmago.backend.models.Producto;
import com.ucb.farmago.backend.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Productos")
@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<ProductoDTO> listarTodos() {
        return productoService.listarTodos().stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/otc")
    public List<ProductoDTO> listarOtc() {
        return productoService.listarOtc().stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new ProductoDTO(productoService.obtenerPorId(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ProductoDTO(productoService.crear(producto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            return ResponseEntity.ok(new ProductoDTO(productoService.actualizar(id, producto)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/categoria/{categoria}")
    public List<ProductoDTO> listarPorCategoria(@PathVariable String categoria) {
        return productoService.listarPorCategoria(categoria).stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList());
    }
}
