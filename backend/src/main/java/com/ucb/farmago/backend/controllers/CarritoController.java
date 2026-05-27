package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.dto.CarritoDTO;
import com.ucb.farmago.backend.services.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/{clienteId}")
    public ResponseEntity<?> verCarrito(@PathVariable Long clienteId) {
        try {
            return ResponseEntity.ok(new CarritoDTO(carritoService.verCarrito(clienteId)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{clienteId}/agregar")
    public ResponseEntity<?> agregar(@PathVariable Long clienteId,
                                     @RequestParam Long productoId,
                                     @RequestParam Integer cantidad) {
        try {
            return ResponseEntity.ok(new CarritoDTO(carritoService.agregar(clienteId, productoId, cantidad)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{clienteId}/modificar/{productoId}")
    public ResponseEntity<?> modificar(@PathVariable Long clienteId,
                                       @PathVariable Long productoId,
                                       @RequestParam Integer cantidad) {
        try {
            return ResponseEntity.ok(new CarritoDTO(carritoService.modificar(clienteId, productoId, cantidad)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{clienteId}/quitar/{productoId}")
    public ResponseEntity<?> quitar(@PathVariable Long clienteId,
                                    @PathVariable Long productoId) {
        try {
            return ResponseEntity.ok(new CarritoDTO(carritoService.quitar(clienteId, productoId)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{clienteId}/vaciar")
    public ResponseEntity<?> vaciar(@PathVariable Long clienteId) {
        try {
            carritoService.vaciar(clienteId);
            return ResponseEntity.ok("Carrito vaciado");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
