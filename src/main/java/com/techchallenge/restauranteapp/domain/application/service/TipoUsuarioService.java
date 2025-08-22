package com.techchallenge.restauranteapp.domain.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.techchallenge.restauranteapp.domain.exception.RecursoNaoEncontradoException;
import com.techchallenge.restauranteapp.domain.model.TipoUsuario;
import com.techchallenge.restauranteapp.domain.repository.TipoUsuarioRepository;

@Service
public class TipoUsuarioService {

	private final TipoUsuarioRepository tipoUsuarioRepository;

	public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository) {
		this.tipoUsuarioRepository = tipoUsuarioRepository;
	}

	public TipoUsuario criar(TipoUsuario tipoUsuario) {
		return tipoUsuarioRepository.salvar(tipoUsuario);
	}

	public List<TipoUsuario> listarTodos() {
		return tipoUsuarioRepository.listarTodos();
	}

	public TipoUsuario buscarPorIdOuFalhar(Long id) {
		return tipoUsuarioRepository.buscarPorId(id).orElseThrow(
				() -> new RecursoNaoEncontradoException("Tipo de usuário com ID " + id + " não encontrado."));
	}

	public TipoUsuario deletar(Long id) {
		TipoUsuario existente = buscarPorIdOuFalhar(id);
		tipoUsuarioRepository.deletar(existente.getId());
		return existente;
	}

	public TipoUsuario atualizar(Long id, TipoUsuario tipoUsuario) {
		TipoUsuario existente = buscarPorIdOuFalhar(id);
		existente.setNomeTipo(tipoUsuario.getNomeTipo());
		return tipoUsuarioRepository.salvar(existente);
	}
}
