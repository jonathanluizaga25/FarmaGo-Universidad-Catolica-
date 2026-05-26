package com.ucb.farmago.backend.services;
import com.ucb.farmago.backend.models.Producto;
import com.ucb.farmago.backend.repositories.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
public class CarritoService {

    private final ProductoRepository productoRepository;

    public CarritoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public void agregar(Long productoId, boolean tieneReceta) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if ("ETICO".equals(producto.getCategoria()) && !tieneReceta) {
            throw new RuntimeException(
                "El producto " + producto.getNombre() +
                " es un medicamento etico y requiere receta medica."
            );
        }
    }
}