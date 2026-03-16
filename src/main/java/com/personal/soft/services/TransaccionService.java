package com.personal.soft.services;

import com.personal.soft.repositories.ClienteRepository;
import com.personal.soft.models.Transaccion;
import com.personal.soft.repositories.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.personal.soft.exceptions.EntidadNoEncontradaException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionService {
    private final TransaccionRepository transaccionRepository;
    private final ClienteRepository clienteRepository;

    public Transaccion registrarTransaccion(String clienteId, String fondoId, Transaccion.TipoTransaccion tipo, BigDecimal monto) {
        Transaccion t = Transaccion.builder()
                .clienteId(clienteId)
                .fondoId(fondoId)
                .tipo(tipo)
                .monto(monto)
                .fecha(LocalDateTime.now())
                .build();
        return transaccionRepository.save(t);
    }

    public List<Transaccion> obtenerHistorial(String clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new EntidadNoEncontradaException("El cliente con ID " + clienteId + " no existe");
        }
        return transaccionRepository.findByClienteIdOrderByFechaDesc(clienteId);
    }
}
