package com.personal.soft.services;

import com.personal.soft.exceptions.FondoNoEncontradoException;
import com.personal.soft.exceptions.ReglaNegocioException;
import com.personal.soft.exceptions.SaldoInsuficienteException;
import com.personal.soft.models.Cliente;
import com.personal.soft.models.Fondo;
import com.personal.soft.models.SuscripcionActiva;
import com.personal.soft.models.Transaccion;
import com.personal.soft.repositories.ClienteRepository;
import com.personal.soft.repositories.FondoRepository;
import com.personal.soft.services.notificaciones.NotificacionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FondoService {

    private final FondoRepository fondoRepository;
    private final ClienteRepository clienteRepository;
    private final TransaccionService transaccionService;
    private final NotificacionContext notificacionContext;

    public void suscribirse(String clienteId, String fondoId, BigDecimal montoInversion) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ReglaNegocioException("Cliente no encontrado"));

        Fondo fondo = fondoRepository.findById(fondoId)
                .orElseThrow(() -> new FondoNoEncontradoException(fondoId));

        if (montoInversion.compareTo(fondo.getMontoMinimo()) < 0) {
            throw new ReglaNegocioException("El monto a invertir (" + montoInversion + 
                    ") es menor al mínimo requerido por el fondo: " + fondo.getMontoMinimo());
        }

        if (cliente.getSaldo().compareTo(montoInversion) < 0) {
            throw new SaldoInsuficienteException(fondo.getNombre());
        }

        boolean yaSuscrito = cliente.getSuscripcionesActivas().stream()
                .anyMatch(s -> s.getFondoId().equals(fondoId));
        if (yaSuscrito) {
            throw new ReglaNegocioException("El cliente ya está suscrito al fondo " + fondo.getNombre());
        }

        cliente.setSaldo(cliente.getSaldo().subtract(montoInversion));
        
        SuscripcionActiva nuevaSuscripcion = SuscripcionActiva.builder()
                .fondoId(fondoId)
                .montoInvertido(montoInversion)
                .fechaVinculacion(LocalDateTime.now())
                .build();
        
        cliente.getSuscripcionesActivas().add(nuevaSuscripcion);
        clienteRepository.save(cliente);

        transaccionService.registrarTransaccion(clienteId, fondoId, Transaccion.TipoTransaccion.APERTURA, montoInversion);

        notificacionContext.notificarSuscripcion(cliente, fondo.getNombre(), montoInversion);
    }

    public void cancelarSuscripcion(String clienteId, String fondoId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ReglaNegocioException("Cliente no encontrado"));
                
        Fondo fondo = fondoRepository.findById(fondoId)
                .orElseThrow(() -> new FondoNoEncontradoException(fondoId));

        Optional<SuscripcionActiva> suscripcionOpt = cliente.getSuscripcionesActivas().stream()
                .filter(s -> s.getFondoId().equals(fondoId))
                .findFirst();

        if (suscripcionOpt.isEmpty()) {
            throw new ReglaNegocioException("El cliente no está suscrito al fondo " + fondo.getNombre());
        }

        SuscripcionActiva suscripcion = suscripcionOpt.get();
        BigDecimal montoADevolver = suscripcion.getMontoInvertido();

        cliente.getSuscripcionesActivas().remove(suscripcion);
        cliente.setSaldo(cliente.getSaldo().add(montoADevolver));
        clienteRepository.save(cliente);

        transaccionService.registrarTransaccion(clienteId, fondoId, Transaccion.TipoTransaccion.CANCELACION, montoADevolver);
        
        notificacionContext.notificarCancelacion(cliente, fondo.getNombre(), montoADevolver);
    }
}
