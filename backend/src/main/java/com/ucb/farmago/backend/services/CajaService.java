package com.ucb.farmago.backend.services;
import com.ucb.farmago.backend.models.Caja;
import com.ucb.farmago.backend.models.Usuario;
import com.ucb.farmago.backend.repositories.CajaRepository;
import com.ucb.farmago.backend.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CajaService {

    private static final List<String> TURNOS_VALIDOS = List.of("MANANA", "TARDE", "NOCHE", "DIA_COMPLETO");

    private final CajaRepository cajaRepository;
    private final UsuarioRepository usuarioRepository;

    public CajaService(CajaRepository cajaRepository, UsuarioRepository usuarioRepository) {
        this.cajaRepository = cajaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Caja abrirCaja(Long cajeroId, String turno) {
        String turnoNormalizado = turno.trim().toUpperCase();
        if (!TURNOS_VALIDOS.contains(turnoNormalizado)) {
            throw new RuntimeException("Turno invalido. Valores permitidos: MANANA, TARDE, NOCHE, DIA_COMPLETO");
        }
        if (cajaRepository.existsByCajeroIdAndCerradoFalse(cajeroId)) {
            throw new RuntimeException("El cajero ya tiene una caja abierta. Debe cerrarla antes de abrir una nueva.");
        }
        Usuario cajero = usuarioRepository.findById(cajeroId)
                .orElseThrow(() -> new RuntimeException("Cajero no encontrado"));
        Caja caja = new Caja();
        caja.setCajero(cajero);
        caja.setFecha(LocalDate.now());
        caja.setTurno(turnoNormalizado);
        caja.setTotalEfectivo(BigDecimal.ZERO);
        caja.setTotalQr(BigDecimal.ZERO);
        caja.setCerrado(false);
        return cajaRepository.save(caja);
    }

    public Caja registrarPago(Long cajaId, BigDecimal montoEfectivo, BigDecimal montoQr) {
        Caja caja = cajaRepository.findById(cajaId)
                .orElseThrow(() -> new RuntimeException("Caja no encontrada"));
        if (caja.getCerrado()) {
            throw new RuntimeException("La caja ya esta cerrada.");
        }
        caja.setTotalEfectivo(caja.getTotalEfectivo().add(montoEfectivo));
        caja.setTotalQr(caja.getTotalQr().add(montoQr));
        return cajaRepository.save(caja);
    }

    public Caja cerrarCaja(Long cajaId) {
        Caja caja = cajaRepository.findById(cajaId)
                .orElseThrow(() -> new RuntimeException("Caja no encontrada"));
        if (caja.getCerrado()) {
            throw new RuntimeException("La caja ya esta cerrada.");
        }
        caja.setCerrado(true);
        return cajaRepository.save(caja);
    }

    public List<Caja> listarPorCajero(Long cajeroId) {
        return cajaRepository.findByCajeroId(cajeroId);
    }

    public List<Caja> listarAbiertas() {
        return cajaRepository.findByCerrado(false);
    }

    public Caja obtenerPorId(Long id) {
        return cajaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Caja no encontrada"));
    }
}