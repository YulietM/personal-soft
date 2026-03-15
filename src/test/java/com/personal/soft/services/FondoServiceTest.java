package com.personal.soft.services;

import com.personal.soft.exceptions.ReglaNegocioException;
import com.personal.soft.exceptions.SaldoInsuficienteException;
import com.personal.soft.models.Cliente;
import com.personal.soft.models.Fondo;
import com.personal.soft.models.SuscripcionActiva;
import com.personal.soft.repositories.ClienteRepository;
import com.personal.soft.repositories.FondoRepository;
import com.personal.soft.services.notificaciones.NotificacionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FondoServiceTest {

    @Mock
    private FondoRepository fondoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private TransaccionService transaccionService;

    @Mock
    private NotificacionContext notificacionContext;

    @InjectMocks
    private FondoService fondoService;

    private Cliente clienteTest;
    private Fondo fondoTest;

    @BeforeEach
    void setUp() {
        clienteTest = Cliente.builder()
                .id("c1")
                .nombre("Juan")
                .saldo(new BigDecimal("500000"))
                .preferenciaNotificacion(Cliente.TipoNotificacion.EMAIL)
                .suscripcionesActivas(new ArrayList<>())
                .build();

        fondoTest = Fondo.builder()
                .id("f1")
                .nombre("Fondo Acciones")
                .montoMinimo(new BigDecimal("250000"))
                .build();
    }

    @Test
    void suscribirse_DeberiaReducirSaldoYAgregarSuscripcion_CuandoEsValido() {
        // Given
        BigDecimal montoInversion = new BigDecimal("300000"); // >= minimo (250000)
        when(clienteRepository.findById("c1")).thenReturn(Optional.of(clienteTest));
        when(fondoRepository.findById("f1")).thenReturn(Optional.of(fondoTest));

        // When
        fondoService.suscribirse("c1", "f1", montoInversion);

        // Then
        assertEquals(new BigDecimal("200000"), clienteTest.getSaldo());
        assertEquals(1, clienteTest.getSuscripcionesActivas().size());
        assertEquals("f1", clienteTest.getSuscripcionesActivas().get(0).getFondoId());
        
        verify(clienteRepository).save(clienteTest);
        verify(transaccionService).registrarTransaccion(eq("c1"), eq("f1"), any(), eq(montoInversion));
        verify(notificacionContext).notificarSuscripcion(clienteTest, "Fondo Acciones");
    }

    @Test
    void suscribirse_DeberiaLanzarExcepcion_CuandoSaldoEsInsuficiente() {
        // Given
        clienteTest.setSaldo(new BigDecimal("100000")); // Saldo menor a la inversión
        BigDecimal montoInversion = new BigDecimal("300000");
        
        when(clienteRepository.findById("c1")).thenReturn(Optional.of(clienteTest));
        when(fondoRepository.findById("f1")).thenReturn(Optional.of(fondoTest));

        // When & Then
        assertThrows(SaldoInsuficienteException.class, 
                     () -> fondoService.suscribirse("c1", "f1", montoInversion));
        
        verify(clienteRepository, never()).save(any());
        verify(transaccionService, never()).registrarTransaccion(any(), any(), any(), any());
    }

    @Test
    void suscribirse_DeberiaLanzarExcepcion_CuandoMontoMenorAlMinimo() {
        // Given
        BigDecimal montoInversion = new BigDecimal("100000"); // Menor al mínimo de 250k
        
        when(clienteRepository.findById("c1")).thenReturn(Optional.of(clienteTest));
        when(fondoRepository.findById("f1")).thenReturn(Optional.of(fondoTest));

        // When & Then
        assertThrows(ReglaNegocioException.class, 
                     () -> fondoService.suscribirse("c1", "f1", montoInversion));
    }

    @Test
    void cancelarSuscripcion_DeberiaDevolverSaldo_CuandoSuscripcionExiste() {
        // Given
        BigDecimal montoInvertidoPrevio = new BigDecimal("300000");
        clienteTest.setSaldo(new BigDecimal("200000")); // Quedó con 200k
        clienteTest.getSuscripcionesActivas().add(
                SuscripcionActiva.builder().fondoId("f1").montoInvertido(montoInvertidoPrevio).build()
        );

        when(clienteRepository.findById("c1")).thenReturn(Optional.of(clienteTest));
        when(fondoRepository.findById("f1")).thenReturn(Optional.of(fondoTest));

        // When
        fondoService.cancelarSuscripcion("c1", "f1");

        // Then
        assertEquals(new BigDecimal("500000"), clienteTest.getSaldo());
        assertTrue(clienteTest.getSuscripcionesActivas().isEmpty());
        
        verify(clienteRepository).save(clienteTest);
        verify(transaccionService).registrarTransaccion(eq("c1"), eq("f1"), any(), eq(montoInvertidoPrevio));
    }
}
