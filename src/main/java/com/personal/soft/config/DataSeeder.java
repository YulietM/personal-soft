package com.personal.soft.config;

import com.personal.soft.models.Cliente;
import com.personal.soft.models.Fondo;
import com.personal.soft.repositories.ClienteRepository;
import com.personal.soft.repositories.FondoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final FondoRepository fondoRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public void run(String... args) throws Exception {
        if (fondoRepository.count() == 0) {
            log.info("Inicializando fondos base...");
            fondoRepository.saveAll(List.of(
                Fondo.builder().id("1").nombre("FPV_BTG_PACTUAL_RECAUDADORA").montoMinimo(new BigDecimal("75000")).categoria("FPV").build(),
                Fondo.builder().id("2").nombre("FPV_BTG_PACTUAL_ECOPETROL").montoMinimo(new BigDecimal("125000")).categoria("FPV").build(),
                Fondo.builder().id("3").nombre("DEUDAPRIVADA").montoMinimo(new BigDecimal("50000")).categoria("FIC").build(),
                Fondo.builder().id("4").nombre("FDO-ACCIONES").montoMinimo(new BigDecimal("250000")).categoria("FIC").build(),
                Fondo.builder().id("5").nombre("FPV_BTG_PACTUAL_DINAMICA").montoMinimo(new BigDecimal("100000")).categoria("FPV").build()
            ));
        }

        if (clienteRepository.count() == 0) {
            log.info("Inicializando cliente de prueba...");
            clienteRepository.save(Cliente.builder()
                .nombre("Usuario de Prueba")
                .email("usuario@btgpactual.com")
                .celular("3001234567")
                .saldo(new BigDecimal("500000"))
                .preferenciaNotificacion(Cliente.TipoNotificacion.EMAIL)
                .build());
        }
    }
}
