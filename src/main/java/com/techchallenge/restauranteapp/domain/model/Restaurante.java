package com.techchallenge.restauranteapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Restaurante {
	private Long id;
	private String nome;
	private String cnpj;

	private String endereco;
	private String tipoCozinha;
	private String horarioFuncionamento;

	private Long donoUsuarioId;
}