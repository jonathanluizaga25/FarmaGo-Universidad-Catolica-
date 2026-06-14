package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.dto.AlertaDTO;
import com.ucb.farmago.backend.services.AlertaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Alertas")
@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @PostMapping("/verificar")
    public ResponseEntity<String> verificarDesabastecimiento() {
        alertaService.verificarDesabastecimiento();
        return ResponseEntity.ok("Verificacion completada");
    }

    @GetMapping("/no-leidas")
    public ResponseEntity<List<AlertaDTO>> getAlertasNoLeidas() {
        return ResponseEntity.ok(alertaService.getAlertasNoLeidas().stream()
                .map(AlertaDTO::new)
                .collect(Collectors.toList()));
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<String> marcarComoLeida(@PathVariable Long id) {
        alertaService.marcarComoLeida(id);
        return ResponseEntity.ok("Alerta marcada como leida");
    }
}
