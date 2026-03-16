package com.personal.soft.services.notificaciones;

import sendinblue.ApiClient;
import sendinblue.Configuration;
import sibApi.TransactionalEmailsApi;
import sendinblue.auth.ApiKeyAuth;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service("EMAIL")
public class EmailNotificacionStrategy implements NotificacionStrategy {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Override
    public void enviarNotificacion(String destinatario, Map<String, Object> datos) {
        String mensaje = (String) datos.get("mensaje");
        String fondo = (String) datos.get("fondo");
        String monto = (String) datos.get("monto");
        String saldo = (String) datos.get("saldo");
        String tipo = (String) datos.get("tipo"); // Suscripción / Cancelación

        log.info("📧 [ENVIANDO EMAIL a {}] -> {}", destinatario, mensaje);

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKeyAuth.setApiKey(apiKey);

        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();

        SendSmtpEmailSender sender = new SendSmtpEmailSender();
        sender.setEmail(senderEmail);
        sender.setName("BTG Pactual Notificaciones");
        sendSmtpEmail.setSender(sender);

        SendSmtpEmailTo to = new SendSmtpEmailTo();
        to.setEmail(destinatario);
        sendSmtpEmail.setTo(Collections.singletonList(to));

        sendSmtpEmail.setSubject("Comprobante de Movimiento - " + fondo);

        // Template HTML Enriquecido y Premium
        String htmlContent = 
            "<div style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f8fafc; padding: 40px; color: #1e293b;\">" +
            "  <div style=\"max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.08);\">" +
            "    <div style=\"background: linear-gradient(135deg, #003366 0%, #004080 100%); padding: 40px; text-align: center;\">" +
            "      <h1 style=\"color: #ffffff; margin: 0; font-size: 28px; font-weight: 700; letter-spacing: 2px; text-transform: uppercase;\">BTG Pactual</h1>" +
            "      <p style=\"color: #bae6fd; margin-top: 10px; font-size: 14px;\">Comprobante Digital de Transacción</p>" +
            "    </div>" +
            "    <div style=\"padding: 40px;\">" +
            "      <h2 style=\"color: #0f172a; margin-top: 0; font-size: 20px;\">Hola,</h2>" +
            "      <p style=\"font-size: 16px; line-height: 1.6; color: #475569;\">" + mensaje + "</p>" +
            "      " +
            "      <div style=\"margin: 30px 0; background-color: #f1f5f9; border-radius: 8px; padding: 25px;\">" +
            "        <h3 style=\"margin-top: 0; font-size: 14px; text-transform: uppercase; color: #94a3b8; letter-spacing: 1px;\">Detalles de la Operación</h3>" +
            "        <table style=\"width: 100%; border-collapse: collapse; margin-top: 15px;\">" +
            "          <tr>" +
            "            <td style=\"padding: 8px 0; color: #64748b; font-size: 14px;\">Fondo:</td>" +
            "            <td style=\"padding: 8px 0; color: #1e293b; font-size: 14px; font-weight: 600; text-align: right;\">" + fondo + "</td>" +
            "          </tr>" +
            "          <tr>" +
            "            <td style=\"padding: 8px 0; color: #64748b; font-size: 14px;\">Monto Operación:</td>" +
            "            <td style=\"padding: 8px 0; color: #0369a1; font-size: 14px; font-weight: 700; text-align: right;\">$" + monto + "</td>" +
            "          </tr>" +
            "          <tr>" +
            "            <td style=\"padding: 8px 0; color: #64748b; font-size: 14px;\">Saldo Disponible:</td>" +
            "            <td style=\"padding: 8px 0; color: #1e293b; font-size: 14px; font-weight: 600; text-align: right;\">$" + saldo + "</td>" +
            "          </tr>" +
            "          <tr>" +
            "            <td style=\"padding: 15px 0 0 0; color: #64748b; font-size: 12px; font-style: italic;\" colspan=\"2\">Fecha: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "</td>" +
            "          </tr>" +
            "        </table>" +
            "      </div>" +
            "      " +
            "      <div style=\"border-top: 1px solid #e2e8f0; padding-top: 30px; margin-top: 30px;\">" +
            "        <p style=\"font-size: 13px; color: #94a3b8; line-height: 1.5;\">" +
            "          <strong>Importante:</strong> Esta información es estrictamente confidencial. BTG Pactual nunca le solicitará sus claves por este medio." +
            "        </p>" +
            "      </div>" +
            "    </div>" +
            "    <div style=\"background-color: #f1f5f9; padding: 20px; text-align: center;\">" +
            "      <p style=\"color: #94a3b8; font-size: 11px; margin: 0;\">" +
            "        &copy; 2026 BTG Pactual S.A. Comisionista de Bolsa.<br/>" +
            "        Este es un mensaje generado automáticamente." +
            "      </p>" +
            "    </div>" +
            "  </div>" +
            "</div>";

        sendSmtpEmail.setHtmlContent(htmlContent);

        try {
            apiInstance.sendTransacEmail(sendSmtpEmail);
            log.info("✅ Email enriquecido enviado exitosamente a {}", destinatario);
        } catch (Exception e) {
            log.error("❌ Error enviando email enriquecido a {}: {}", destinatario, e.getMessage());
        }
    }
}
