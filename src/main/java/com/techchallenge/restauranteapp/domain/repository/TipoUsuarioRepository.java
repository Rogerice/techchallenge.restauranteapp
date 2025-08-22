package com.techchallenge.restauranteapp.domain.repository;

import com.techchallenge.restauranteapp.domain.model.TipoUsuario;

import java.util.List;
import java.util.Optional;

public interface TipoUsuarioRepository {
	TipoUsuario salvar(TipoUsuario tipoUsuario);

	List<TipoUsuario> listarTodos();

	Optional<TipoUsuario> buscarPorId(Long id);

	void deletar(Long id);
}
