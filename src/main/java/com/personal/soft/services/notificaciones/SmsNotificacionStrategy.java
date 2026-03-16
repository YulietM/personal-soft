package com.personal.soft.services.notificaciones;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@Slf4j
@Service("SMS")
public class SmsNotificacionStrategy implements NotificacionStrategy {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @PostConstruct
    public void init() {
        try {
            Twilio.init(accountSid, authToken);
            log.info("🚀 Twilio inicializado correctamente");
        } catch (Exception e) {
            log.error("❌ Error inicializando Twilio: {}", e.getMessage());
        }
    }

    @Override
    public void enviarNotificacion(String destinatario, Map<String, Object> datos) {
        String mensaje = (String) datos.get("mensaje");
        String fondo = (String) datos.get("fondo");
        String monto = (String) datos.get("monto");
        
        // Formatear mensaje para SMS (máximo impacto en poco espacio)
        String textoSms = String.format("BTG Pactual: %s en %s por $%s. Tu saldo actual: $%s", 
            mensaje, fondo, monto, datos.get("saldo"));

        log.info("📱 [ENVIANDO SMS a {}] -> {}", destinatario, textoSms);

        try {
            Message.creator(
                new PhoneNumber(destinatario),
                new PhoneNumber(twilioPhoneNumber),
                textoSms
            ).create();
            log.info("✅ SMS enviado exitosamente via Twilio a {}", destinatario);
        } catch (Exception e) {
            log.error("❌ Error enviando SMS via Twilio a {}: {}", destinatario, e.getMessage());
        }
    }
}
