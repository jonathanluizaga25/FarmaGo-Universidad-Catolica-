package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.services.SupervisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/supervision")
@CrossOrigin(origins = "*")
public class SupervisionController {

    private final SupervisionService supervisionService;

    public SupervisionController(SupervisionService supervisionService) {
        this.supervisionService = supervisionService;
    }

    // HU-19: Panel integrado de supervision para el administrador
    @GetMapping("/panel")
    public ResponseEntity<Map<String, Object>> obtenerPanel() {
        return ResponseEntity.ok(supervisionService.obtenerPanelSupervision());
    }
}