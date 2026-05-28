package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.dto.SeguimientoPedidoDTO;
import com.ucb.farmago.backend.services.SeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Seguimiento")
@RestController
@RequestMapping("/api/seguimiento")
@CrossOrigin(origins = "*")
public class SeguimientoController {

    @Autowired
    private SeguimientoService seguimientoService;

    // GET /api/seguimiento/cliente/3
    // Devuelve todos los pedidos del cliente con estado
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<SeguimientoPedidoDTO>> pedidosCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(seguimientoService.obtenerPedidosCliente(clienteId));
    }

    // GET /api/seguimiento/pedido/5?clienteId=3
    // Devuelve el detalle completo de un pedido específico
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> detallePedido(
            @PathVariable Long pedidoId,
            @RequestParam Long clienteId) {
        try {
            return ResponseEntity.ok(seguimientoService.obtenerDetallePedido(pedidoId, clienteId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
