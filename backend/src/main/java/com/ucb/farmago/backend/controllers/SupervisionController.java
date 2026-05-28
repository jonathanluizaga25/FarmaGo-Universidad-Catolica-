package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.services.SupervisionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "Supervision")
@RestController
@RequestMapping("/api/supervision")
@CrossOrigin(origins = "*")
public class SupervisionController {

    private final SupervisionService supervisionService;

    public SupervisionController(SupervisionService supervisionService) {
        this.supervisionService = supervisionService;
    }

    @GetMapping("/panel")
    public ResponseEntity<Map<String, Object>> obtenerPanel() {
        return ResponseEntity.ok(supervisionService.obtenerPanelSupervision());
    }
}
