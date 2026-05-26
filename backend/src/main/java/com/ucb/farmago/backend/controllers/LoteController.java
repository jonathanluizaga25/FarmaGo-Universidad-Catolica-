package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.dto.LoteDTO;
import com.ucb.farmago.backend.models.Alerta;
import com.ucb.farmago.backend.models.Lote;
import com.ucb.farmago.backend.services.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lotes")
@CrossOrigin(origins = "*")
public class LoteController {

    @Autowired
    private LoteService loteService;

    @GetMapping
    public List<LoteDTO> listarTodos() {
        return loteService.listarTodos().stream()
                .map(LoteDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new LoteDTO(loteService.obtenerPorId(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<LoteDTO> crear(@RequestBody Lote lote) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoteDTO(loteService.crear(lote)));
    }

    @GetMapping("/producto/{productoId}")
    public List<LoteDTO> listarPorProducto(@PathVariable Long productoId) {
        return loteService.listarPorProducto(productoId).stream()
                .map(LoteDTO::new)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        try {
            return ResponseEntity.ok(new LoteDTO(loteService.actualizarEstado(id, estado)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // HU-13: Verificar vencimientos y generar alertas escalonadas
    @PostMapping("/verificar-vencimientos")
    public ResponseEntity<List<Alerta>> verificarVencimientos() {
        List<Alerta> alertas = loteService.verificarVencimientos();
        return ResponseEntity.ok(alertas);
    }
}