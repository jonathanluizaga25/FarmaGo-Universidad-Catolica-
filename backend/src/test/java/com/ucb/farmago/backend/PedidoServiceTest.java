package com.ucb.farmago.backend;
import com.ucb.farmago.backend.repositories.CarritoRepository;
import com.ucb.farmago.backend.repositories.DetallePedidoRepository;
import com.ucb.farmago.backend.repositories.PedidoRepository;
import com.ucb.farmago.backend.repositories.ProductoRepository;
import com.ucb.farmago.backend.repositories.UsuarioRepository;
import com.ucb.farmago.backend.services.PedidoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private DetallePedidoRepository detallePedidoRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void calcularCostoEnvio_zonaCentro_esCero() {
        BigDecimal costo = pedidoService.calcularCostoEnvio("centro");
        assertEquals(BigDecimal.ZERO, costo);
    }

    @Test
    void calcularCostoEnvio_zonaNorte_esDiez() {
        BigDecimal costo = pedidoService.calcularCostoEnvio("norte");
        assertEquals(new BigDecimal("10"), costo);
    }

    @Test
    void calcularCostoEnvio_zonaLejana_esQuince() {
        BigDecimal costo = pedidoService.calcularCostoEnvio("villa");
        assertEquals(new BigDecimal("15"), costo);
    }

    @Test
    void calcularCostoEnvio_nulo_esQuince() {
        BigDecimal costo = pedidoService.calcularCostoEnvio(null);
        assertEquals(new BigDecimal("15"), costo);
    }
}