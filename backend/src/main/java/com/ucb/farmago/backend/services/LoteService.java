package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.models.Lote;
import com.ucb.farmago.backend.repositories.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoteService {

    @Autowired
    private LoteRepository loteRepository;

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
}
