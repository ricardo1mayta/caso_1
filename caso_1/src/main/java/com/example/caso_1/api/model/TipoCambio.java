package com.example.caso_1.api.model;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table("tipo_cambio")
public class TipoCambio {

    @Id
    private Long id;
    private String fromCurrency;
    private String toCurrency;
    private double rate;

    // Getters y Setters
}