package com.ucb.farmago.backend.controllers;
import com.ucb.farmago.backend.models.Familiar;
import com.ucb.farmago.backend.services.FamiliarService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Tag(name = "Familiares")
@RestController
@RequestMapping("/api/familiares")
@CrossOrigin(origins = "*")
public class FamiliarController {
    @Autowired
    private FamiliarService familiarService;
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> agregar(@PathVariable Long usuarioId, @RequestBody Familiar familiar) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(familiarService.agregar(usuarioId, familiar));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Familiar>> listar(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(familiarService.listarPorUsuario(usuarioId));
    }
    @DeleteMapping("/usuario/{usuarioId}/familiar/{familiarId}")
    public ResponseEntity<?> eliminar(@PathVariable Long usuarioId, @PathVariable Long familiarId) {
        try {
            familiarService.eliminarDe(usuarioId, familiarId);
            return ResponseEntity.ok("Familiar eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}