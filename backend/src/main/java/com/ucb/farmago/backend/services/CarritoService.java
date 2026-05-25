package com.ucb.farmago.backend.services;

import com.ucb.farmago.backend.models.*;
import com.ucb.farmago.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    private Carrito obtenerOCrearCarrito(Long clienteId) {
        return carritoRepository.findByClienteIdAndEstado(clienteId, "ACTIVO")
                .orElseGet(() -> {
                    Usuario cliente = usuarioRepository.findById(clienteId)
                            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                    Carrito nuevo = new Carrito();
                    nuevo.setCliente(cliente);
                    return carritoRepository.save(nuevo);
                });
    }

    public Carrito verCarrito(Long clienteId) {
        return obtenerOCrearCarrito(clienteId);
    }

    public Carrito agregar(Long clienteId, Long productoId, Integer cantidad) {
        Carrito carrito = obtenerOCrearCarrito(clienteId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if ("ETICO".equals(producto.getTipo())) {
            throw new RuntimeException(
                    "El producto " + producto.getNombre() +
                            " es un medicamento ético y requiere receta médica. " +
                            "No puede agregarse al carrito en línea."
            );
        }

        if (producto.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        carritoItemRepository.findByCarritoIdAndProductoId(carrito.getId(), productoId)
                .ifPresentOrElse(
                        item -> {
                            item.setCantidad(item.getCantidad() + cantidad);
                            carritoItemRepository.save(item);
                        },
                        () -> {
                            CarritoItem item = new CarritoItem();
                            item.setCarrito(carrito);
                            item.setProducto(producto);
                            item.setCantidad(cantidad);
                            item.setPrecioUnitario(producto.getPrecio());
                            carritoItemRepository.save(item);
                        }
                );

        return carritoRepository.findById(carrito.getId()).get();
    }

    public Carrito modificar(Long clienteId, Long productoId, Integer cantidad) {
        Carrito carrito = obtenerOCrearCarrito(clienteId);

        CarritoItem item = carritoItemRepository
                .findByCarritoIdAndProductoId(carrito.getId(), productoId)
                .orElseThrow(() -> new RuntimeException("Producto no esta en el carrito"));

        if (cantidad <= 0) {
            carritoItemRepository.delete(item);
        } else {
            Producto producto = productoRepository.findById(productoId).get();
            if (producto.getStockActual() < cantidad) {
                throw new RuntimeException("Stock insuficiente");
            }
            item.setCantidad(cantidad);
            carritoItemRepository.save(item);
        }

        return carritoRepository.findById(carrito.getId()).get();
    }

    public Carrito quitar(Long clienteId, Long productoId) {
        Carrito carrito = obtenerOCrearCarrito(clienteId);

        CarritoItem item = carritoItemRepository
                .findByCarritoIdAndProductoId(carrito.getId(), productoId)
                .orElseThrow(() -> new RuntimeException("Producto no esta en el carrito"));

        carritoItemRepository.delete(item);
        return carritoRepository.findById(carrito.getId()).get();
    }

    public void vaciar(Long clienteId) {
        carritoRepository.findByClienteIdAndEstado(clienteId, "ACTIVO")
                .ifPresent(c -> {
                    c.getItems().clear();
                    carritoRepository.save(c);
                });
    }
}