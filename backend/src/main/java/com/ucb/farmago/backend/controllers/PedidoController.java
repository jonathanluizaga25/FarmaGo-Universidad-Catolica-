package com.ucb.farmago.backend.controllers;

import com.ucb.farmago.backend.dto.FacturaValidacionDTO;
import com.ucb.farmago.backend.dto.HistorialPedidoDTO;
import com.ucb.farmago.backend.dto.PedidoDTO;
import com.ucb.farmago.backend.dto.ResultadoValidacionDTO;
import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Pedidos")
@RestController
@RequestMapping("/api/pedidos")
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
    public ResponseEntity<?> crear(@RequestBody Pedido pedido) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new PedidoDTO(pedidoService.crear(pedido)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        try {
            return ResponseEntity.ok(new PedidoDTO(pedidoService.actualizarEstado(id, estado)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // HU-A1: Asignar un repartidor a un pedido pendiente desde el panel de supervision
    // PUT /api/pedidos/{id}/asignar  body: { "repartidorId": 5 }
    @PutMapping("/{id}/asignar")
    public ResponseEntity<?> asignarRepartidor(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        try {
            Long repartidorId = body.get("repartidorId");
            if (repartidorId == null) {
                return ResponseEntity.badRequest().body("Se requiere el campo repartidorId");
            }
            return ResponseEntity.ok(new PedidoDTO(pedidoService.asignarRepartidor(id, repartidorId)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new PedidoDTO(pedidoService.cancelar(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validar-factura")
    public ResponseEntity<ResultadoValidacionDTO> validarFactura(@RequestBody FacturaValidacionDTO facturaDTO) {
        try {
            return ResponseEntity.ok(pedidoService.validarFacturaVsPedido(facturaDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResultadoValidacionDTO("ERROR", e.getMessage()));
        }
    }

    @GetMapping("/cliente/{clienteId}/historial")
    public ResponseEntity<List<HistorialPedidoDTO>> obtenerHistorialCompras(@PathVariable Long clienteId) {
        List<HistorialPedidoDTO> historial = pedidoService.obtenerHistorialCliente(clienteId);
        if (historial.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(historial);
    }
}

