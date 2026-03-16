package com.personal.soft.services;

import com.personal.soft.dtos.ActualizarClienteRequest;
import com.personal.soft.dtos.CrearClienteRequest;
import com.personal.soft.exceptions.EntidadNoEncontradaException;
import com.personal.soft.exceptions.EntidadYaExisteException;
import com.personal.soft.models.Cliente;
import com.personal.soft.repositories.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        assertThrows(EntidadNoEncontradaException.class, () -> clienteService.obtenerPorId("c1"));
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

    @Test
    void crearCliente_DeberiaLanzarExcepcion_CuandoIdentificacionYaExiste() {
        // Given
        CrearClienteRequest request = new CrearClienteRequest();
        request.setIdentificacion(1234567890L);
        when(clienteRepository.findByIdentificacion(1234567890L)).thenReturn(Optional.of(new Cliente()));

        // When & Then
        assertThrows(EntidadYaExisteException.class, () -> clienteService.crearCliente(request));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void actualizarCliente_DeberiaActualizarEmailYCelular() {
        // Given
        Cliente cliente = Cliente.builder().id("c1").email("viejo@test.com").celular("123").build();
        ActualizarClienteRequest request = new ActualizarClienteRequest();
        request.setEmail("nuevo@test.com");
        request.setCelular("456");

        when(clienteRepository.findById("c1")).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Cliente resultado = clienteService.actualizarCliente("c1", request);

        // Then
        assertEquals("nuevo@test.com", resultado.getEmail());
        assertEquals("456", resultado.getCelular());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void actualizarCliente_NoDeberiaActualizar_CuandoCamposSonNulos() {
        // Given
        Cliente cliente = Cliente.builder().id("c1").email("viejo@test.com").celular("123").build();
        ActualizarClienteRequest request = new ActualizarClienteRequest();

        when(clienteRepository.findById("c1")).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Cliente resultado = clienteService.actualizarCliente("c1", request);

        // Then
        assertEquals("viejo@test.com", resultado.getEmail());
        assertEquals("123", resultado.getCelular());
    }
}
