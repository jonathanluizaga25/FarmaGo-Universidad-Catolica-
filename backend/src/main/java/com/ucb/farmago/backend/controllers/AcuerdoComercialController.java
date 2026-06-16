package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.dto.AcuerdoComercialDTO;
import com.ucb.farmago.backend.repositories.AcuerdoComercialRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Acuerdos Comerciales")
@RestController
@RequestMapping("/api/acuerdos")
public class AcuerdoComercialController {

    @Autowired
    private AcuerdoComercialRepository acuerdoComercialRepository;

    // Devuelve los acuerdos activos para poblar el selector del form de descuentos
    @GetMapping("/activos")
    public ResponseEntity<List<AcuerdoComercialDTO>> listarActivos() {
        List<AcuerdoComercialDTO> lista = acuerdoComercialRepository
                .findByActivo(true)
                .stream()
                .map(AcuerdoComercialDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }
}