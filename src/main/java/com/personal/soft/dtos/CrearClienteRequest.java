package com.personal.soft.dtos;

import com.personal.soft.models.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CrearClienteRequest {
    @NotNull(message = "La identificación es obligatoria")
    @jakarta.validation.constraints.Max(value = 9999999999L, message = "La identificación no puede tener más de 10 dígitos")
    private Long identificacion;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "El celular es obligatorio")
    @jakarta.validation.constraints.Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "El celular debe incluir el indicativo de país empezando con '+' (ej: +57310...)")
    private String celular;

    @NotNull(message = "La preferencia de notificación es obligatoria (EMAIL o SMS)")
    private Cliente.TipoNotificacion preferenciaNotificacion;

    @NotNull(message = "El monto inicial es obligatorio")
    @jakarta.validation.constraints.DecimalMin(value = "500000.0", message = "El monto de apertura no puede ser menor a $500.000 COP")
    private java.math.BigDecimal saldo;
}
