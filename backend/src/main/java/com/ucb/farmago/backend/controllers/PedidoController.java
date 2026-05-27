package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.dto.PedidoDTO;
import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<PedidoDTO> listarTodos() {
        return pedidoService.listarTodos().stream()
                .map(PedidoDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new PedidoDTO(pedidoService.obtenerPorId(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> crear(@RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new PedidoDTO(pedidoService.crear(pedido)));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        try {
            return ResponseEntity.ok(new PedidoDTO(pedidoService.actualizarEstado(id, estado)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public List<PedidoDTO> listarPorCliente(@PathVariable Long clienteId) {
        return pedidoService.listarPorCliente(clienteId).stream()
                .map(PedidoDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/repartidor/{repartidorId}")
    public List<PedidoDTO> listarPorRepartidor(@PathVariable Long repartidorId) {
        return pedidoService.listarPorRepartidor(repartidorId).stream()
                .map(PedidoDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/estado/{estado}")
    public List<PedidoDTO> listarPorEstado(@PathVariable String estado) {
        return pedidoService.listarPorEstado(estado).stream()
                .map(PedidoDTO::new)
                .collect(Collectors.toList());
    }

    // HU-10: Calcular costo de envio segun direccion
    @GetMapping("/costo-envio")
    public ResponseEntity<BigDecimal> calcularCostoEnvio(@RequestParam String direccion) {
        return ResponseEntity.ok(pedidoService.calcularCostoEnvio(direccion));
    }

    // HU-12: Cancelar pedido si esta en estado Pendiente
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new PedidoDTO(pedidoService.cancelar(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
