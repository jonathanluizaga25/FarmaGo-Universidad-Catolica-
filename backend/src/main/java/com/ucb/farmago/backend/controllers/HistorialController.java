package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.dto.HistorialDTO;
import com.ucb.farmago.backend.services.HistorialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialController {

    private final HistorialService historialService;

    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<List<HistorialDTO>> getHistorial(
            @PathVariable Long clienteId) {
        return ResponseEntity.ok(historialService.getHistorialCliente(clienteId));
    }
}
