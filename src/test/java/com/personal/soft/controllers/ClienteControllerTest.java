package com.personal.soft.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.soft.dtos.CrearClienteRequest;
import com.personal.soft.models.Cliente;
import com.personal.soft.services.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void listarClientes_DeberiaRetornar200() throws Exception {
        when(clienteService.listarTodos()).thenReturn(List.of(new Cliente()));

        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void obtenerCliente_DeberiaRetornar200() throws Exception {
        Cliente cliente = Cliente.builder().id("c1").nombre("Juan").build();
        when(clienteService.obtenerPorId("c1")).thenReturn(cliente);

        mockMvc.perform(get("/api/v1/clientes/c1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void crearCliente_DeberiaRetornar201() throws Exception {
        CrearClienteRequest request = new CrearClienteRequest();
        request.setNombre("Test");
        request.setEmail("test@test.com");
        request.setCelular("12345678");
        request.setSaldo(new BigDecimal("500000"));
        request.setPreferenciaNotificacion(Cliente.TipoNotificacion.EMAIL);

        Cliente clienteResponse = Cliente.builder().id("id1").nombre("Test").build();
        when(clienteService.crearCliente(any())).thenReturn(clienteResponse);

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Test"));
    }
}
