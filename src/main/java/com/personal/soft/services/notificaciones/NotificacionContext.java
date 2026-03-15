package com.personal.soft.services.notificaciones;

import com.personal.soft.models.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificacionContext {

    private final Map<String, NotificacionStrategy> strategies;

    public void notificarSuscripcion(Cliente cliente, String nombreFondo) {
        String tipo = cliente.getPreferenciaNotificacion().name();
        NotificacionStrategy strategy = strategies.get(tipo);
        
        if (strategy != null) {
            String destinatario = tipo.equals("EMAIL") ? cliente.getEmail() : cliente.getCelular();
            strategy.enviarNotificacion(destinatario, "Te has suscrito exitosamente al fondo: " + nombreFondo);
        }
    }
}
