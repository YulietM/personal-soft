package com.personal.soft.services.notificaciones;

import com.personal.soft.models.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificacionContext {

    private final Map<String, NotificacionStrategy> strategies;

    public void notificarSuscripcion(Cliente cliente, String nombreFondo, BigDecimal monto) {
        Map<String, Object> datos = buildDatos(cliente, nombreFondo, monto, "Suscripción");
        datos.put("mensaje", "Te has suscrito exitosamente al fondo: " + nombreFondo);
        enviar(cliente, datos);
    }

    public void notificarCancelacion(Cliente cliente, String nombreFondo, BigDecimal monto) {
        Map<String, Object> datos = buildDatos(cliente, nombreFondo, monto, "Cancelación");
        datos.put("mensaje", "Has cancelado tu suscripción al fondo: " + nombreFondo);
        enviar(cliente, datos);
    }

    private Map<String, Object> buildDatos(Cliente cliente, String nombreFondo, BigDecimal monto, String tipo) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("fondo", nombreFondo);
        datos.put("monto", monto.toString());
        datos.put("saldo", cliente.getSaldo().toString());
        datos.put("tipo", tipo);
        return datos;
    }

    private void enviar(Cliente cliente, Map<String, Object> datos) {
        String tipo = cliente.getPreferenciaNotificacion().name();
        NotificacionStrategy strategy = strategies.get(tipo);
        
        if (strategy != null) {
            String destinatario = tipo.equals("EMAIL") ? cliente.getEmail() : cliente.getCelular();
            strategy.enviarNotificacion(destinatario, datos);
        }
    }
}
