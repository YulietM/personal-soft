package com.personal.soft.services.notificaciones;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("SMS")
public class SmsNotificacionStrategy implements NotificacionStrategy {
    @Override
    public void enviarNotificacion(String destinatario, String mensaje) {
        log.info("📱 [ENVIANDO SMS a {}] -> {}", destinatario, mensaje);
    }
}
