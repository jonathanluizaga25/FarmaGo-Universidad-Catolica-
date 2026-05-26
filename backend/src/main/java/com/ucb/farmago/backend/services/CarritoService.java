package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.models.Producto;
import com.ucb.farmago.backend.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarritoService {

    private final ProductoRepository productoRepository;

    public CarritoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public void validarProducto(Long productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if ("ETICO".equals(producto.getCategoria())) {
            throw new RuntimeException(
                "El producto " + producto.getNombre() +
                " es un medicamento etico y requiere receta medica. " +
                "No puede ser adquirido en linea."
            );
        }
    }

    public List<Producto> listarProductosOtc() {
        return productoRepository.findByCategoria("OTC");
    }
}
