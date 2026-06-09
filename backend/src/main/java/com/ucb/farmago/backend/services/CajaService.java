package com.ucb.farmago.backend.services;
<<<<<<< HEAD

=======
>>>>>>> origin/main
import com.ucb.farmago.backend.models.Caja;
import com.ucb.farmago.backend.models.Usuario;
import com.ucb.farmago.backend.repositories.CajaRepository;
import com.ucb.farmago.backend.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
<<<<<<< HEAD

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
=======
import java.math.BigDecimal;
import java.time.LocalDate;
>>>>>>> origin/main
import java.util.List;

@Service
public class CajaService {

    private final CajaRepository cajaRepository;
    private final UsuarioRepository usuarioRepository;

    public CajaService(CajaRepository cajaRepository, UsuarioRepository usuarioRepository) {
        this.cajaRepository = cajaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Caja abrirCaja(Long cajeroId, String turno) {
<<<<<<< HEAD
        List<String> turnosValidos = Arrays.asList("MANANA", "TARDE", "NOCHE", "DIA_COMPLETO");
        String turnoLimpio = turno.toUpperCase().trim();
        
        if (!turnosValidos.contains(turnoLimpio)) {
            throw new RuntimeException("Turno inválido. Solo se permite: MANANA, TARDE, NOCHE o DIA_COMPLETO");
        }

        if (cajaRepository.existsByCajeroIdAndCerradoFalse(cajeroId)) {
            throw new RuntimeException("El cajero ya tiene una caja abierta. Debe cerrarla antes de abrir una nueva.");
        }

        Usuario cajero = usuarioRepository.findById(cajeroId)
                .orElseThrow(() -> new RuntimeException("Cajero no encontrado"));

        Caja caja = new Caja();
        caja.setCajero(cajero);
        caja.setFecha(LocalDate.now());
        caja.setTurno(turnoLimpio); 
        caja.setTotalEfectivo(BigDecimal.ZERO);
        caja.setTotalQr(BigDecimal.ZERO);
        caja.setCerrado(false);

=======
        List<Caja> abiertas = cajaRepository.findByCerrado(false);
        boolean tieneAbierta = abiertas.stream().anyMatch(c -> c.getCajero().getId().equals(cajeroId));
        if (tieneAbierta) {
            throw new RuntimeException("El cajero ya tiene una caja abierta. Debe cerrarla antes de abrir una nueva.");
        }
        Usuario cajero = usuarioRepository.findById(cajeroId)
                .orElseThrow(() -> new RuntimeException("Cajero no encontrado"));
        Caja caja = new Caja();
        caja.setCajero(cajero);
        caja.setFecha(LocalDate.now());
        caja.setTurno(turno);
        caja.setTotalEfectivo(BigDecimal.ZERO);
        caja.setTotalQr(BigDecimal.ZERO);
        caja.setCerrado(false);
>>>>>>> origin/main
        return cajaRepository.save(caja);
    }

    public Caja registrarPago(Long cajaId, BigDecimal montoEfectivo, BigDecimal montoQr) {
        Caja caja = cajaRepository.findById(cajaId)
                .orElseThrow(() -> new RuntimeException("Caja no encontrada"));
        if (caja.getCerrado()) {
<<<<<<< HEAD
            throw new RuntimeException("La caja ya está cerrada.");
=======
            throw new RuntimeException("La caja ya esta cerrada.");
>>>>>>> origin/main
        }
        caja.setTotalEfectivo(caja.getTotalEfectivo().add(montoEfectivo));
        caja.setTotalQr(caja.getTotalQr().add(montoQr));
        return cajaRepository.save(caja);
    }

    public Caja cerrarCaja(Long cajaId) {
        Caja caja = cajaRepository.findById(cajaId)
                .orElseThrow(() -> new RuntimeException("Caja no encontrada"));
        if (caja.getCerrado()) {
<<<<<<< HEAD
            throw new RuntimeException("La caja ya está cerrada.");
=======
            throw new RuntimeException("La caja ya esta cerrada.");
>>>>>>> origin/main
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