package com.techchallenge.restauranteapp.domain.repository;

import java.util.*;
import com.techchallenge.restauranteapp.domain.model.Usuario;

public interface UsuarioRepository {
	Usuario salvar(Usuario u);

	Optional<Usuario> buscarPorId(Long id);

	List<Usuario> listarTodos();

	void deletar(Long id);
	
	
}