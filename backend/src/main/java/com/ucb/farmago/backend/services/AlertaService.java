package com.ucb.farmago.backend.services;

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

    @Scheduled(cron = "0 0 9 * * *")
    public void verificarDesabastecimiento() {
        List<Producto> productos = productoRepository.findAll();
        for (Producto producto : productos) {
            Integer stockActual = producto.getStockActual();
            Integer stockMinimo = producto.getStockMinimo();
            if (stockActual != null && stockMinimo != null && stockActual <= stockMinimo) {
                List<Alerta> existentes = alertaRepository.findByProductoId(producto.getId());
                boolean yaExiste = existentes.stream()
                        .anyMatch(a -> "STOCK_MINIMO".equals(a.getTipo()));
                if (!yaExiste) {
                    Alerta alerta = new Alerta();
                    alerta.setTipo("STOCK_MINIMO");
                    alerta.setProducto(producto);
                    alerta.setMensaje("El producto " + producto.getNombre() +
                            " tiene stock " + stockActual +
                            " que es menor o igual al minimo de " + stockMinimo);
                    alerta.setLeida(Boolean.FALSE);
                    alertaRepository.save(alerta);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void verificarVencimientos() {
        List<Lote> lotes = loteRepository.findAll();
        LocalDate hoy = LocalDate.now();

        for (Lote lote : lotes) {
            if (!"DISPONIBLE".equals(lote.getEstado())) continue;

            LocalDate fechaVenc = lote.getFechaVencimiento()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            long meses = ChronoUnit.MONTHS.between(hoy, fechaVenc);

            String nivel = null;
            if (meses <= 2) {
                nivel = "VENCIMIENTO_2M";
            } else if (meses <= 4) {
                nivel = "VENCIMIENTO_4M";
            } else if (meses <= 6) {
                nivel = "VENCIMIENTO_6M";
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

    public List<Alerta> listarTodas() {
        return alertaRepository.findAll();
    }

    public List<Alerta> getAlertasNoLeidas() {
        return alertaRepository.findByLeida(Boolean.FALSE);
    }

    public void marcarComoLeida(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));
        alerta.setLeida(Boolean.TRUE);
        alertaRepository.save(alerta);
    }
}