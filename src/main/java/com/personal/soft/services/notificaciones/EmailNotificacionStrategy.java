package com.personal.soft.services.notificaciones;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("EMAIL")
public class EmailNotificacionStrategy implements NotificacionStrategy {
    @Override
    public void enviarNotificacion(String destinatario, String mensaje) {
        log.info("📧 [ENVIANDO EMAIL a {}] -> {}", destinatario, mensaje);
    }
}
