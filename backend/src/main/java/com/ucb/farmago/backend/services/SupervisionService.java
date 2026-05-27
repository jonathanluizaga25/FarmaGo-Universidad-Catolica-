package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.models.Alerta;
import com.ucb.farmago.backend.models.Descuento;
import com.ucb.farmago.backend.models.Pedido;
import com.ucb.farmago.backend.repositories.AlertaRepository;
import com.ucb.farmago.backend.repositories.DescuentoRepository;
import com.ucb.farmago.backend.repositories.PedidoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SupervisionService {

    private final PedidoRepository pedidoRepository;
    private final AlertaRepository alertaRepository;
    private final DescuentoRepository descuentoRepository;

    public SupervisionService(PedidoRepository pedidoRepository,
                              AlertaRepository alertaRepository,
                              DescuentoRepository descuentoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.alertaRepository = alertaRepository;
        this.descuentoRepository = descuentoRepository;
    }

    // HU-19: Panel integrado de supervision
    public Map<String, Object> obtenerPanelSupervision() {
        // Pedidos pendientes
        List<Pedido> pedidosPendientes = pedidoRepository.findByEstado("PENDIENTE");

        // Alertas no leidas
        List<Alerta> alertasNoLeidas = alertaRepository.findByLeida(Boolean.FALSE);

        // Descuentos vigentes
        LocalDate hoy = LocalDate.now();
        List<Descuento> descuentosVigentes = descuentoRepository.findByActivo(true).stream()
                .filter(d -> !hoy.isBefore(d.getFechaInicio()) && !hoy.isAfter(d.getFechaFin()))
                .collect(Collectors.toList());

        Map<String, Object> panel = new HashMap<>();
        panel.put("pedidosPendientes", pedidosPendientes);
        panel.put("alertasNoLeidas", alertasNoLeidas);
        panel.put("descuentosVigentes", descuentosVigentes);
        panel.put("totalPedidosPendientes", pedidosPendientes.size());
        panel.put("totalAlertasNoLeidas", alertasNoLeidas.size());
        panel.put("totalDescuentosVigentes", descuentosVigentes.size());

        return panel;
    }
}