package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.models.AcuerdoComercial;
import com.ucb.farmago.backend.models.Descuento;
import com.ucb.farmago.backend.models.Producto;
import com.ucb.farmago.backend.repositories.AcuerdoComercialRepository;
import com.ucb.farmago.backend.repositories.DescuentoRepository;
import com.ucb.farmago.backend.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class DescuentoService {

    private final DescuentoRepository descuentoRepository;
    private final ProductoRepository productoRepository;
    private final AcuerdoComercialRepository acuerdoComercialRepository;

    public DescuentoService(DescuentoRepository descuentoRepository,
                            ProductoRepository productoRepository,
                            AcuerdoComercialRepository acuerdoComercialRepository) {
        this.descuentoRepository = descuentoRepository;
        this.productoRepository = productoRepository;
        this.acuerdoComercialRepository = acuerdoComercialRepository;
    }

    public Descuento registrar(Long acuerdoId, Long productoId, Descuento descuento) {
        AcuerdoComercial acuerdo = acuerdoComercialRepository.findById(acuerdoId)
                .orElseThrow(() -> new RuntimeException("Acuerdo comercial no encontrado"));

        if (!acuerdo.getActivo()) {
            throw new RuntimeException("El acuerdo comercial no esta activo. No se puede registrar el descuento.");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        descuento.setAcuerdo(acuerdo);
        descuento.setProducto(producto);
        descuento.setActivo(true);
        return descuentoRepository.save(descuento);
    }

    public List<Descuento> listarActivos() {
        return descuentoRepository.findByActivo(true);
    }

    public List<Descuento> listarPorProducto(Long productoId) {
        return descuentoRepository.findByProductoId(productoId);
    }

    public Descuento desactivar(Long id) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
        descuento.setActivo(false);
        return descuentoRepository.save(descuento);
    }

    public List<Descuento> listarVigentes() {
        LocalDate hoy = LocalDate.now();
        return descuentoRepository.findByActivo(true).stream()
                .filter(d -> !hoy.isBefore(d.getFechaInicio()) && !hoy.isAfter(d.getFechaFin()))
                .toList();
    }
}
