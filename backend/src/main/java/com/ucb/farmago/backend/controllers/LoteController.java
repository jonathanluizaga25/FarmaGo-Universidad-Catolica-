package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.models.Lote;
import com.ucb.farmago.backend.services.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lotes")
@CrossOrigin(origins = "*")
public class LoteController {

    @Autowired
    private LoteService loteService;

    @GetMapping
    public List<Lote> listarTodos() {
        return loteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(loteService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Lote> crear(@RequestBody Lote lote) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loteService.crear(lote));
    }

    @GetMapping("/producto/{productoId}")
    public List<Lote> listarPorProducto(@PathVariable Long productoId) {
        return loteService.listarPorProducto(productoId);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        try {
            return ResponseEntity.ok(loteService.actualizarEstado(id, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
