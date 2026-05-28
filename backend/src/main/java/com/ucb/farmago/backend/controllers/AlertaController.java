package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.models.Alerta;
import com.ucb.farmago.backend.services.AlertaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Alertas")
@RestController
@RequestMapping("/api/alertas")
@CrossOrigin(origins = "http://localhost:3000")
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
    public ResponseEntity<List<Alerta>> getAlertasNoLeidas() {
        return ResponseEntity.ok(alertaService.getAlertasNoLeidas());
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<String> marcarComoLeida(@PathVariable Long id) {
        alertaService.marcarComoLeida(id);
        return ResponseEntity.ok("Alerta marcada como leida");
    }
}
