package com.personal.soft.services;

import com.personal.soft.dtos.CrearClienteRequest;
import com.personal.soft.models.Cliente;
import com.personal.soft.repositories.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void listarTodos_DeberiaRetornarListaDeClientes() {
        // Given
        when(clienteRepository.findAll()).thenReturn(List.of(new Cliente()));

        // When
        List<Cliente> resultado = clienteService.listarTodos();

        // Then
        assertFalse(resultado.isEmpty());
        verify(clienteRepository).findAll();
    }

    @Test
    void obtenerPorId_DeberiaRetornarCliente_CuandoExiste() {
        // Given
        Cliente cliente = Cliente.builder().id("c1").nombre("Prueba").build();
        when(clienteRepository.findById("c1")).thenReturn(Optional.of(cliente));

        // When
        Cliente resultado = clienteService.obtenerPorId("c1");

        // Then
        assertEquals("Prueba", resultado.getNombre());
    }

    @Test
    void obtenerPorId_DeberiaLanzarExcepcion_CuandoNoExiste() {
        // Given
        when(clienteRepository.findById("c1")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseStatusException.class, () -> clienteService.obtenerPorId("c1"));
    }

    @Test
    void crearCliente_DeberiaGuardarYRetornarNuevoCliente() {
        // Given
        CrearClienteRequest request = new CrearClienteRequest();
        request.setNombre("Nuevo");
        request.setSaldo(new BigDecimal("500000"));
        
        Cliente clienteGuardado = Cliente.builder().id("generated-id").nombre("Nuevo").build();
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);

        // When
        Cliente resultado = clienteService.crearCliente(request);

        // Then
        assertNotNull(resultado.getId());
        assertEquals("Nuevo", resultado.getNombre());
        verify(clienteRepository).save(any(Cliente.class));
    }
}
