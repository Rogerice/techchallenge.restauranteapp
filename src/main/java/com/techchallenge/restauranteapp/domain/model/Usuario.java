package com.techchallenge.restauranteapp.domain.model;

import com.techchallenge.restauranteapp.domain.enums.TipoUsuarioEnum;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
	private Long id;
	private String nome;
	private String email;
	private String senha;

    private TipoUsuarioEnum tipoUsuario;
}
