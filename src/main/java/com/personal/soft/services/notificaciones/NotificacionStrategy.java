package com.personal.soft.services.notificaciones;

import java.util.Map;

public interface NotificacionStrategy {
    void enviarNotificacion(String destinatario, Map<String, Object> datos);
}
