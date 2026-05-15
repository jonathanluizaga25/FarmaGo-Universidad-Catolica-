package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.models.Lote;
import com.ucb.farmago.backend.services.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Lote> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(loteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Lote> crear(@RequestBody Lote lote) {
        return ResponseEntity.ok(loteService.crear(lote));
    }

    @GetMapping("/producto/{productoId}")
    public List<Lote> listarPorProducto(@PathVariable Long productoId) {
        return loteService.listarPorProducto(productoId);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Lote> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(loteService.actualizarEstado(id, estado));
    }
}
