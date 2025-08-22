package com.techchallenge.restauranteapp.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipos_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_tipo", nullable = false)
    private String nomeTipo;
}
