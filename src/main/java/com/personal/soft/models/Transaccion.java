package com.personal.soft.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transacciones")
public class Transaccion {
    @Id
    private String id;
    
    @Indexed
    private String clienteId;
    
    private String fondoId;
    private TipoTransaccion tipo;
    private BigDecimal monto;
    private LocalDateTime fecha;

    public enum TipoTransaccion {
        APERTURA, CANCELACION
    }
}
