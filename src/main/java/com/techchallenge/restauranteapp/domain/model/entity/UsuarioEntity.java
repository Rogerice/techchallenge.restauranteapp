package com.techchallenge.restauranteapp.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import com.techchallenge.restauranteapp.domain.enums.TipoUsuarioEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UsuarioEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String senha;

	@Enumerated(EnumType.STRING)
    @Column(name="tipo_usuario", nullable=false, length=20)
    private TipoUsuarioEnum tipoUsuario;
}
