package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.models.Alerta;
import com.ucb.farmago.backend.models.Lote;
import com.ucb.farmago.backend.repositories.AlertaRepository;
import com.ucb.farmago.backend.repositories.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LoteService {

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private AlertaRepository alertaRepository;

    public List<Lote> listarTodos() {
        return loteRepository.findAll();
    }

    public Lote obtenerPorId(Long id) {
        return loteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));
    }

    public Lote crear(Lote lote) {
        return loteRepository.save(lote);
    }

    public List<Lote> listarPorProducto(Long productoId) {
        return loteRepository.findByProductoId(productoId);
    }

    public Lote actualizarEstado(Long id, String estado) {
        Lote lote = obtenerPorId(id);
        lote.setEstado(estado);
        return loteRepository.save(lote);
    }

    // HU-13: Verifica vencimientos y genera alertas escalonadas a 6, 4 y 2 meses
    public List<Alerta> verificarVencimientos() {
        List<Lote> lotes = loteRepository.findAll();
        List<Alerta> alertasGeneradas = new ArrayList<>();
        Date hoy = new Date();

        for (Lote lote : lotes) {
            if (!"DISPONIBLE".equals(lote.getEstado())) continue;

            Date fechaVencimiento = lote.getFechaVencimiento();
            long diasRestantes = (fechaVencimiento.getTime() - hoy.getTime()) / (1000 * 60 * 60 * 24);

            String nivel;
            if (diasRestantes <= 60) {
                nivel = "CRITICO - 2 meses para vencer";
            } else if (diasRestantes <= 120) {
                nivel = "ADVERTENCIA - 4 meses para vencer";
            } else if (diasRestantes <= 180) {
                nivel = "PRECAUCION - 6 meses para vencer";
            } else {
                continue;
            }

            final String nivelFinal = nivel;
            List<Alerta> existentes = alertaRepository.findByProductoId(lote.getProducto().getId());
            boolean yaExiste = existentes.stream()
                    .anyMatch(a -> a.getMensaje().contains(nivelFinal));

            if (!yaExiste) {
                Alerta alerta = new Alerta();
                alerta.setTipo("VENCIMIENTO");
                alerta.setProducto(lote.getProducto());
                alerta.setLote(lote);
                alerta.setMensaje("Lote " + lote.getId() + " de " +
                        lote.getProducto().getNombre() + " - " + nivelFinal);
                alerta.setLeida(Boolean.FALSE);
                alertaRepository.save(alerta);
                alertasGeneradas.add(alerta);
            }
        }
        return alertasGeneradas;
    }
}