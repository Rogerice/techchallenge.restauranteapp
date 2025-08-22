package com.techchallenge.restauranteapp.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestauranteEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;
	@Column(nullable = false, unique = true)
	private String cnpj;

	@Column(nullable = false)
	private String endereco;
	@Column(nullable = false)
	private String tipoCozinha;
	@Column(nullable = false)
	private String horarioFuncionamento;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "dono_usuario_id")
	private UsuarioEntity dono;
}