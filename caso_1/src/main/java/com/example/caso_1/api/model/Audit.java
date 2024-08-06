package com.example.caso_1.api.model;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("audits")
public class Audit {
    @Id
    private Long id;
    private String username;
    private String action;
    private String details;

    // Getters y Setters
}