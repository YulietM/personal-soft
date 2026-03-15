package com.personal.soft.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuscripcionActiva {
    private String fondoId;
    private BigDecimal montoInvertido;
    private LocalDateTime fechaVinculacion;
}
