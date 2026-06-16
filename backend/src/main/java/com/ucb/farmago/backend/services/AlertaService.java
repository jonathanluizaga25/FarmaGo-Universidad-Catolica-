package com.ucb.farmago.backend.services;

// ─── AlertaService — Lógica de negocio de alertas automáticas ────────────────
// Este servicio tiene dos trabajos principales:
//
// 1. ALERTAS AUTOMÁTICAS (@Scheduled):
//    Spring ejecuta estos métodos automáticamente según el cron configurado,
//    sin que nadie tenga que llamarlos. Revisan la BD y crean alertas si es necesario.
//
//    - verificarDesabastecimiento() → corre todos los días a las 9:00am
//      Revisa cada producto. Si stockActual <= stockMinimo, crea alerta STOCK_MINIMO.
//
//    - verificarVencimientos() → corre todos los días a las 8:00am
//      Revisa cada lote disponible. Si vence en ≤6 meses, crea alerta de vencimiento.
//
// 2. CONSULTA Y MARCADO (llamados desde AlertaController):
//    - listarTodas()      → devuelve todas las alertas para el panel admin
//    - getAlertasNoLeidas() → devuelve solo las no leídas (para el badge del sidebar)
//    - marcarComoLeida()  → actualiza leida=true cuando el admin hace clic en "Leer"

import com.ucb.farmago.backend.models.Alerta;
import com.ucb.farmago.backend.models.Lote;
import com.ucb.farmago.backend.models.Producto;
import com.ucb.farmago.backend.repositories.AlertaRepository;
import com.ucb.farmago.backend.repositories.LoteRepository;
import com.ucb.farmago.backend.repositories.ProductoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;

    public AlertaService(AlertaRepository alertaRepository, ProductoRepository productoRepository, LoteRepository loteRepository) {
        this.alertaRepository = alertaRepository;
        this.productoRepository = productoRepository;
        this.loteRepository = loteRepository;
    }

    // ── verificarDesabastecimiento ────────────────────────────────────────────
    // Cron: "0 0 9 * * *" = segundo 0, minuto 0, hora 9, cualquier día/mes/año
    // Recorre todos los productos y crea alerta si el stock bajó del mínimo.
    // Verifica primero que no exista ya una alerta del mismo tipo para ese producto
    // (para no duplicar alertas).
    @Scheduled(cron = "0 0 9 * * *")
    public void verificarDesabastecimiento() {
        List<Producto> productos = productoRepository.findAll();
        for (Producto producto : productos) {
            Integer stockActual = producto.getStockActual();
            Integer stockMinimo = producto.getStockMinimo();
            if (stockActual != null && stockMinimo != null && stockActual <= stockMinimo) {
                // Verificar si ya existe una alerta de stock para este producto
                List<Alerta> existentes = alertaRepository.findByProductoId(producto.getId());
                boolean yaExiste = existentes.stream()
                        .anyMatch(a -> "STOCK_MINIMO".equals(a.getTipo()));
                if (!yaExiste) {
                    // Crear nueva alerta
                    Alerta alerta = new Alerta();
                    alerta.setTipo("STOCK_MINIMO");
                    alerta.setProducto(producto);
                    alerta.setMensaje("El producto " + producto.getNombre() +
                            " tiene stock " + stockActual +
                            " que es menor o igual al minimo de " + stockMinimo);
                    alerta.setLeida(Boolean.FALSE); // nueva alerta = no leída
                    alertaRepository.save(alerta);
                }
            }
        }
    }

    // ── verificarVencimientos ─────────────────────────────────────────────────
    // Cron: "0 0 8 * * *" = todos los días a las 8:00am
    // Revisa lotes en estado DISPONIBLE y calcula cuántos meses faltan para vencer.
    // Genera 3 niveles de alerta: VENCIMIENTO_6M, VENCIMIENTO_4M, VENCIMIENTO_2M
    // (el más urgente, 2 meses, tendrá prioridad visual en el panel).
    @Scheduled(cron = "0 0 8 * * *")
    public void verificarVencimientos() {
        List<Lote> lotes = loteRepository.findAll();
        LocalDate hoy = LocalDate.now();

        for (Lote lote : lotes) {
            if (!"DISPONIBLE".equals(lote.getEstado())) continue; // solo lotes activos

            // Convertir Date a LocalDate para calcular meses
            LocalDate fechaVenc = lote.getFechaVencimiento()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            long meses = ChronoUnit.MONTHS.between(hoy, fechaVenc);

            // Determinar nivel de urgencia
            String nivel = null;
            if (meses <= 2) {
                nivel = "VENCIMIENTO_2M"; // urgente
            } else if (meses <= 4) {
                nivel = "VENCIMIENTO_4M"; // medio
            } else if (meses <= 6) {
                nivel = "VENCIMIENTO_6M"; // advertencia temprana
            }

            if (nivel != null) {
                String tipoFinal = nivel;
                List<Alerta> existentes = alertaRepository.findByProductoId(lote.getProducto().getId());
                boolean yaExiste = existentes.stream()
                        .anyMatch(a -> tipoFinal.equals(a.getTipo()));
                if (!yaExiste) {
                    Alerta alerta = new Alerta();
                    alerta.setTipo(nivel);
                    alerta.setProducto(lote.getProducto());
                    alerta.setLote(lote);
                    alerta.setMensaje("El lote del producto " + lote.getProducto().getNombre() +
                            " vence en " + meses + " meses (" + fechaVenc + ")");
                    alerta.setLeida(Boolean.FALSE);
                    alertaRepository.save(alerta);
                }
            }
        }
    }

    // ── Métodos de consulta (llamados desde AlertaController) ─────────────────

    // Devuelve todas las alertas para mostrar en el panel admin
    public List<Alerta> listarTodas() {
        return alertaRepository.findAll();
    }

    // Devuelve solo alertas no leídas (para el contador del badge en el sidebar)
    public List<Alerta> getAlertasNoLeidas() {
        return alertaRepository.findByLeida(Boolean.FALSE);
    }

    // Marca una alerta como leída cuando el admin hace clic en "Leer"
    public void marcarComoLeida(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));
        alerta.setLeida(Boolean.TRUE);
        alertaRepository.save(alerta);
    }
}
