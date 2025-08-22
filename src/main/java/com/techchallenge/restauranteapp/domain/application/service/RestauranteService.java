package com.techchallenge.restauranteapp.domain.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techchallenge.restauranteapp.domain.exception.RecursoNaoEncontradoException;
import com.techchallenge.restauranteapp.domain.exception.ValidacaoException;
import com.techchallenge.restauranteapp.domain.model.Restaurante;
import com.techchallenge.restauranteapp.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {

    private final RestauranteRepository repository;

    public RestauranteService(RestauranteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Restaurante criar(Restaurante restaurante) {
        if (restaurante.getDonoUsuarioId() == null) {
            throw new ValidacaoException("donoUsuarioId é obrigatório");
        }
        return repository.salvar(restaurante);
    }

    public List<Restaurante> listarTodos() {
        return repository.listarTodos();
    }

    public Restaurante buscarPorId(Long id) {
        return repository.buscarPorId(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado"));
    }

    @Transactional
    public Restaurante atualizar(Long id, Restaurante restaurante) {
        Restaurante existente = repository.buscarPorId(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado"));

        existente.setNome(restaurante.getNome());
        existente.setCnpj(restaurante.getCnpj());
        existente.setEndereco(restaurante.getEndereco());
        existente.setTipoCozinha(restaurante.getTipoCozinha());
        existente.setHorarioFuncionamento(restaurante.getHorarioFuncionamento());
        existente.setDonoUsuarioId(restaurante.getDonoUsuarioId());

        return repository.salvar(existente);
    }

    @Transactional
    public void deletar(Long id) {
        if (repository.buscarPorId(id).isEmpty()) {
            throw new RecursoNaoEncontradoException("Restaurante não encontrado");
        }
        repository.deletar(id);
    }
    
    
}
