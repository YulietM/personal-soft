package com.personal.soft.services;

import com.personal.soft.models.Transaccion;
import com.personal.soft.repositories.ClienteRepository;
import com.personal.soft.repositories.TransaccionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.personal.soft.exceptions.EntidadNoEncontradaException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private TransaccionService transaccionService;

    @Test
    void registrarTransaccion_DeberiaGuardarTransaccion() {
        // Given
        Transaccion mockT = Transaccion.builder().id("t1").build();
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(mockT);

        // When
        Transaccion resultado = transaccionService.registrarTransaccion("c1", "f1",
                Transaccion.TipoTransaccion.APERTURA, new BigDecimal("50000"));

        // Then
        assertNotNull(resultado);
        verify(transaccionRepository).save(any(Transaccion.class));
    }

    @Test
    void obtenerHistorial_DeberiaRetornarLista_CuandoClienteExiste() {
        // Given
        when(clienteRepository.existsById("c1")).thenReturn(true);
        when(transaccionRepository.findByClienteIdOrderByFechaDesc("c1")).thenReturn(List.of(new Transaccion()));

        // When
        List<Transaccion> resultado = transaccionService.obtenerHistorial("c1");

        // Then
        assertFalse(resultado.isEmpty());
        verify(transaccionRepository).findByClienteIdOrderByFechaDesc("c1");
    }

    @Test
    void obtenerHistorial_DeberiaLanzarExcepcion_CuandoClienteNoExiste() {
        // Given
        when(clienteRepository.existsById("c1")).thenReturn(false);

        // When & Then
        assertThrows(EntidadNoEncontradaException.class, () -> transaccionService.obtenerHistorial("c1"));
        verify(transaccionRepository, never()).findByClienteIdOrderByFechaDesc(anyString());
    }
}
