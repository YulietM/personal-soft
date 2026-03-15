package com.personal.soft.services.notificaciones;

public interface NotificacionStrategy {
    void enviarNotificacion(String destinatario, String mensaje);
}
