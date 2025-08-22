package com.techchallenge.restauranteapp.domain.model;

import lombok.*;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemCardapio {
	private Long id;

	@NotBlank(message = "nome é obrigatório")
	private String nome;

	private String descricao;

	@NotNull(message = "preco é obrigatório")
	@DecimalMin(value = "0.01", message = "preco deve ser maior que zero")
	private BigDecimal preco;

	private Boolean disponivel;
	private String caminhoFoto;

	private Restaurante restaurante;

	private Boolean somenteNoLocal;
}
