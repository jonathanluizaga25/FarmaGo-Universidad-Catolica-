package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.dto.PedidoRepartidorDTO;
import com.ucb.farmago.backend.services.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Tag(name = "Repartidor")
@RestController
@RequestMapping("/api/repartidor")
@CrossOrigin(origins = "*")
public class RepartidorController {

    @Autowired
    private RepartidorService repartidorService;

    // GET /api/repartidor/2/pedidos
    // Todos los pedidos asignados al repartidor
    @GetMapping("/{repartidorId}/pedidos")
    public ResponseEntity<List<PedidoRepartidorDTO>> pedidosAsignados(
            @PathVariable Long repartidorId) {
        return ResponseEntity.ok(repartidorService.obtenerPedidosAsignados(repartidorId));
    }

    // GET /api/repartidor/2/pedidos?estado=EN_CAMINO
    // Pedidos filtrados por estado
    @GetMapping("/{repartidorId}/pedidos/estado")
    public ResponseEntity<List<PedidoRepartidorDTO>> pedidosPorEstado(
            @PathVariable Long repartidorId,
            @RequestParam String estado) {
        return ResponseEntity.ok(repartidorService.obtenerPedidosPorEstado(repartidorId, estado));
    }

    // PUT /api/repartidor/2/entregas/5/estado
    // El repartidor actualiza el estado de una entrega
    @PutMapping("/{repartidorId}/entregas/{entregaId}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long repartidorId,
            @PathVariable Long entregaId,
            @RequestBody Map<String, String> body) {
        try {
            String estadoEntrega = body.get("estado");
            String observacion = body.get("observacion");
            return ResponseEntity.ok(
                repartidorService.actualizarEstadoEntrega(entregaId, estadoEntrega, observacion, repartidorId)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
