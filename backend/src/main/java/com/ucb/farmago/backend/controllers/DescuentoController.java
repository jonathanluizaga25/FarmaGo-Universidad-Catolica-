package com.ucb.farmago.backend.controllers;
import com.ucb.farmago.backend.models.Descuento;
import com.ucb.farmago.backend.services.DescuentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Descuentos")
@RestController
@RequestMapping("/api/descuentos")
@CrossOrigin(origins = "*")
public class DescuentoController {

    private final DescuentoService descuentoService;

    public DescuentoController(DescuentoService descuentoService) {
        this.descuentoService = descuentoService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<Descuento> registrar(@RequestParam Long acuerdoId,
                                               @RequestParam Long productoId,
                                               @RequestBody Descuento descuento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(descuentoService.registrar(acuerdoId, productoId, descuento));
    }

    @GetMapping
    public List<Descuento> listarActivos() {
        return descuentoService.listarActivos();
    }

    @GetMapping("/vigentes")
    public List<Descuento> listarVigentes() {
        return descuentoService.listarVigentes();
    }

    @GetMapping("/producto/{productoId}")
    public List<Descuento> listarPorProducto(@PathVariable Long productoId) {
        return descuentoService.listarPorProducto(productoId);
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Descuento> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(descuentoService.desactivar(id));
    }
}