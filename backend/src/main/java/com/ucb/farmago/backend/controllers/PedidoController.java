package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.crear(pedido));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
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
