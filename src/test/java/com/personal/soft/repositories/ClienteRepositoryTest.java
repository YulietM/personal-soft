package com.personal.soft.repositories;

import com.personal.soft.entities.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void guardarYBuscarCliente_DeberiaFuncionar() {
        Cliente cliente = Cliente.builder()
                .id("test-id")
                .nombre("Test User")
                .saldo(new BigDecimal("1000"))
                .build();

        clienteRepository.save(cliente);
        Optional<Cliente> encontrado = clienteRepository.findById("test-id");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNombre()).isEqualTo("Test User");
    }
}
