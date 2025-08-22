
package com.techchallenge.restauranteapp.domain.repository;

import com.techchallenge.restauranteapp.domain.model.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository {
	Restaurante salvar(Restaurante restaurante);

	List<Restaurante> listarTodos();

	Optional<Restaurante> buscarPorId(Long id);

	void deletar(Long id);
}
