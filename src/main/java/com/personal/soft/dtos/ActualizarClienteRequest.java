package com.personal.soft.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActualizarClienteRequest {
    @Email(message = "El email no tiene un formato válido")
    private String email;
    
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "El celular debe incluir el indicativo de país empezando con '+' (ej: +57310...)")
    private String celular;
}
