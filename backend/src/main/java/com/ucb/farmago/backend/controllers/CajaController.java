package com.ucb.farmago.backend.controllers;
import com.ucb.farmago.backend.models.Caja;
import com.ucb.farmago.backend.services.CajaService;
<<<<<<< HEAD
=======
import io.swagger.v3.oas.annotations.tags.Tag;
>>>>>>> origin/main
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

<<<<<<< HEAD
=======
@Tag(name = "Caja")
>>>>>>> origin/main
@RestController
@RequestMapping("/api/caja")
@CrossOrigin(origins = "*")
public class CajaController {

    private final CajaService cajaService;

    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }

    @PostMapping("/abrir")
    public ResponseEntity<Caja> abrir(@RequestParam Long cajeroId, @RequestParam String turno) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cajaService.abrirCaja(cajeroId, turno));
    }

    @PutMapping("/{id}/pago")
    public ResponseEntity<Caja> registrarPago(@PathVariable Long id,
                                              @RequestParam BigDecimal montoEfectivo,
                                              @RequestParam BigDecimal montoQr) {
        return ResponseEntity.ok(cajaService.registrarPago(id, montoEfectivo, montoQr));
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<Caja> cerrar(@PathVariable Long id) {
        return ResponseEntity.ok(cajaService.cerrarCaja(id));
    }

    @GetMapping("/cajero/{cajeroId}")
    public List<Caja> listarPorCajero(@PathVariable Long cajeroId) {
        return cajaService.listarPorCajero(cajeroId);
    }

    @GetMapping("/abiertas")
    public List<Caja> listarAbiertas() {
        return cajaService.listarAbiertas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caja> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(cajaService.obtenerPorId(id));
    }
}