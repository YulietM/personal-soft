package com.personal.soft.controllers;

import com.personal.soft.models.Transaccion;
import com.personal.soft.services.TransaccionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransaccionController.class)
class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransaccionService transaccionService;

    @Test
    void obtenerHistorial_DeberiaRetornar200() throws Exception {
        when(transaccionService.obtenerHistorial("c1")).thenReturn(List.of(new Transaccion()));

        mockMvc.perform(get("/api/v1/transacciones/cliente/c1"))
                .andExpect(status().isOk());
    }
}
