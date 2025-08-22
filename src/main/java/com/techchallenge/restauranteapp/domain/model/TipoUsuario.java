package com.techchallenge.restauranteapp.domain.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoUsuario {
    private Long id;
    private String nomeTipo;
}
