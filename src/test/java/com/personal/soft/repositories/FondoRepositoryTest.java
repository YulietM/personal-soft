package com.personal.soft.repositories;

import com.personal.soft.models.Fondo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class FondoRepositoryTest {

    @Autowired
    private FondoRepository fondoRepository;

    @Test
    void buscarTodosLosFondos_DeberiaFuncionar() {
        Fondo fondo = Fondo.builder()
                .id("fondo-test")
                .nombre("Fondo de Prueba")
                .montoMinimo(new BigDecimal("100"))
                .categoria("Prueba")
                .build();
        fondoRepository.save(fondo);

        List<Fondo> fondos = fondoRepository.findAll();

        assertThat(fondos).isNotEmpty();
        assertThat(fondos).extracting(Fondo::getNombre).contains("Fondo de Prueba");
    }
}
