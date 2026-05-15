package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pedidoService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(pedido));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        try {
            return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> listarPorCliente(@PathVariable Long clienteId) {
        return pedidoService.listarPorCliente(clienteId);
    }

    @GetMapping("/repartidor/{repartidorId}")
    public List<Pedido> listarPorRepartidor(@PathVariable Long repartidorId) {
        return pedidoService.listarPorRepartidor(repartidorId);
    }

    @GetMapping("/estado/{estado}")
    public List<Pedido> listarPorEstado(@PathVariable String estado) {
        return pedidoService.listarPorEstado(estado);
    }
}
