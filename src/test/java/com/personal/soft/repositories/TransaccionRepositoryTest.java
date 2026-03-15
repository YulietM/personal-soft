package com.personal.soft.repositories;

import com.personal.soft.entities.Transaccion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class TransaccionRepositoryTest {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Test
    void buscarPorClienteId_DeberiaFuncionar() {
        Transaccion trans = Transaccion.builder()
                .clienteId("cliente-123")
                .nombreFondo("Fondo Test")
                .monto(new BigDecimal("1000"))
                .tipo("APERTURA")
                .fecha(LocalDateTime.now())
                .build();
        transaccionRepository.save(trans);

        List<Transaccion> historial = transaccionRepository.findByClienteId("cliente-123");

        assertThat(historial).isNotEmpty();
        assertThat(historial.get(0).getClienteId()).isEqualTo("cliente-123");
    }
}
