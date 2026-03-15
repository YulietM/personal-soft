package com.personal.soft.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.soft.dtos.SuscripcionRequest;
import com.personal.soft.models.Fondo;
import com.personal.soft.repositories.FondoRepository;
import com.personal.soft.services.FondoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FondoController.class)
class FondoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FondoService fondoService;

    @MockitoBean
    private FondoRepository fondoRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void listarFondos_DeberiaRetornar200() throws Exception {
        when(fondoRepository.findAll()).thenReturn(List.of(new Fondo()));

        mockMvc.perform(get("/api/v1/fondos"))
                .andExpect(status().isOk());
    }

    @Test
    void suscribirse_DeberiaRetornar200() throws Exception {
        SuscripcionRequest request = new SuscripcionRequest();
        request.setClienteId("c1");
        request.setFondoId("f1");
        request.setMonto(new BigDecimal("50000"));

        doNothing().when(fondoService).suscribirse("c1", "f1", new BigDecimal("50000"));

        mockMvc.perform(post("/api/v1/fondos/suscripciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void cancelarSuscripcion_DeberiaRetornar200() throws Exception {
        doNothing().when(fondoService).cancelarSuscripcion("c1", "f1");

        mockMvc.perform(delete("/api/v1/fondos/f1/suscripciones")
                        .param("clienteId", "c1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").exists());
    }
}
