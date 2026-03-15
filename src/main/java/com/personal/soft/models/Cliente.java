package com.personal.soft.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "clientes")
public class Cliente {
    @Id
    private String id;
    private String nombre;
    private String email;
    private String celular;
    private BigDecimal saldo;
    private TipoNotificacion preferenciaNotificacion;
    
    @Builder.Default
    private List<SuscripcionActiva> suscripcionesActivas = new ArrayList<>();

    public enum TipoNotificacion {
        EMAIL, SMS
    }
}
