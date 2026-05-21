package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.models.Alerta;
import com.ucb.farmago.backend.models.Producto;
import com.ucb.farmago.backend.repositories.AlertaRepository;
import com.ucb.farmago.backend.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final ProductoRepository productoRepository;

    public AlertaService(AlertaRepository alertaRepository, ProductoRepository productoRepository) {
        this.alertaRepository = alertaRepository;
        this.productoRepository = productoRepository;
    }

    public void verificarDesabastecimiento() {
        List<Producto> productos = productoRepository.findAll();
        for (Producto producto : productos) {
            Integer stockActual = producto.getStockActual();
            Integer stockMinimo = producto.getStockMinimo();
            if (stockActual != null && stockMinimo != null && stockActual <= stockMinimo) {
                List<Alerta> existentes = alertaRepository.findByProductoId(producto.getId());
                if (existentes.isEmpty()) {
                    Alerta alerta = new Alerta();
                    alerta.setTipo("DESABASTECIMIENTO");
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

    public List<Alerta> getAlertasNoLeidas() {
        return alertaRepository.findByLeida(Boolean.FALSE);
    }

    public void marcarComoLeida(Long id) {
        Alerta alerta = alertaRepository.findById(id).orElseThrow();
        alerta.setLeida(Boolean.TRUE);
        alertaRepository.save(alerta);
    }
}