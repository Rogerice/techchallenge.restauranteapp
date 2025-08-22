package com.techchallenge.restauranteapp.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "itens_cardapio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCardapioEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;
	private String descricao;
	@Column(nullable = false)
	private BigDecimal preco;
	private Boolean disponivel;

	@Column(name = "caminho_foto")
	private String caminhoFoto;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "restaurante_id", nullable = false)
	private RestauranteEntity restaurante;

	@Column(name = "somente_no_local", nullable = false)
	private Boolean somenteNoLocal;
}
