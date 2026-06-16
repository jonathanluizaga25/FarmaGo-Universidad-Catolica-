package com.ucb.farmago.backend.controllers;

// ─── AlertaController — Endpoints de alertas del sistema ─────────────────────
// Las alertas se generan automáticamente (AlertaService con @Scheduled).
// Este controlador solo permite consultarlas y marcarlas como leídas.
// TODOS los endpoints requieren rol ADMINISTRADOR (ver SecurityConfig).
//
// Endpoints:
//   GET  /api/alertas           → todas las alertas (leídas y no leídas)
//   GET  /api/alertas/no-leidas → solo las pendientes (para el badge del sidebar)
//   POST /api/alertas/verificar → dispara la verificación de stock manualmente
//   PUT  /api/alertas/{id}/leer → marca una alerta como leída

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
@CrossOrigin(origins = "*")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    // ── GET /api/alertas ──────────────────────────────────────────────────────
    // Devuelve todas las alertas. El panel admin las muestra en la sección Alertas.
    // AlertaDTO evita exponer la entidad completa (con relaciones Hibernate).
    @GetMapping
    public ResponseEntity<List<AlertaDTO>> getTodasAlertas() {
        return ResponseEntity.ok(alertaService.listarTodas().stream()
                .map(AlertaDTO::new)
                .collect(Collectors.toList()));
    }

    // ── POST /api/alertas/verificar ───────────────────────────────────────────
    // Ejecuta manualmente la verificación de stock bajo.
    // Normalmente se ejecuta automáticamente a las 9am (cron en AlertaService).
    // Útil para testing y para la demo.
    @PostMapping("/verificar")
    public ResponseEntity<String> verificarDesabastecimiento() {
        alertaService.verificarDesabastecimiento();
        return ResponseEntity.ok("Verificacion completada");
    }

    // ── GET /api/alertas/no-leidas ────────────────────────────────────────────
    // Devuelve solo las alertas que el admin aún no leyó.
    // Se usa para el badge rojo con número en el sidebar del panel.
    @GetMapping("/no-leidas")
    public ResponseEntity<List<AlertaDTO>> getAlertasNoLeidas() {
        return ResponseEntity.ok(alertaService.getAlertasNoLeidas().stream()
                .map(AlertaDTO::new)
                .collect(Collectors.toList()));
    }

    // ── PUT /api/alertas/{id}/leer ────────────────────────────────────────────
    // Marca una alerta específica como leída (campo leida = true en la BD).
    // El admin hace clic en "Leer" en el panel → desaparece el badge.
    @PutMapping("/{id}/leer")
    public ResponseEntity<String> marcarComoLeida(@PathVariable Long id) {
        alertaService.marcarComoLeida(id);
        return ResponseEntity.ok("Alerta marcada como leida");
    }
}
