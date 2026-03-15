package com.personal.soft.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "fondos")
public class Fondo {
    @Id
    private String id;
    private String nombre;
    private BigDecimal montoMinimo;
    private String categoria;
}
