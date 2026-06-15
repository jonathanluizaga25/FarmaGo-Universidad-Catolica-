package com.ucb.farmago.backend;
import com.ucb.farmago.backend.models.Producto;
import com.ucb.farmago.backend.repositories.ProductoRepository;
import com.ucb.farmago.backend.services.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void buscarPorNombre_devuelveResultados() {
        Producto p = new Producto();
        p.setNombre("Paracetamol");
        when(productoRepository.findByNombreContainingIgnoreCase("para")).thenReturn(List.of(p));
        List<Producto> resultado = productoService.buscarPorNombre("para");
        assertEquals(1, resultado.size());
        assertEquals("Paracetamol", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorNombre_sinResultados_devuelveListaVacia() {
        when(productoRepository.findByNombreContainingIgnoreCase("xyz")).thenReturn(List.of());
        List<Producto> resultado = productoService.buscarPorNombre("xyz");
        assertTrue(resultado.isEmpty());
    }
}