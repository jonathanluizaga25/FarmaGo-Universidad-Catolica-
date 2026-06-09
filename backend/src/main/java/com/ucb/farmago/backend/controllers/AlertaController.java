package com.ucb.farmago.backend.controllers;

<<<<<<< HEAD
import com.ucb.farmago.backend.models.Alerta;
import com.ucb.farmago.backend.services.AlertaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@CrossOrigin(origins = "http://localhost:3000")
=======
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
>>>>>>> origin/main
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
<<<<<<< HEAD
    public ResponseEntity<List<Alerta>> getAlertasNoLeidas() {
        return ResponseEntity.ok(alertaService.getAlertasNoLeidas());
=======
    public ResponseEntity<List<AlertaDTO>> getAlertasNoLeidas() {
        return ResponseEntity.ok(alertaService.getAlertasNoLeidas().stream()
                .map(AlertaDTO::new)
                .collect(Collectors.toList()));
>>>>>>> origin/main
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<String> marcarComoLeida(@PathVariable Long id) {
        alertaService.marcarComoLeida(id);
        return ResponseEntity.ok("Alerta marcada como leida");
    }
}
