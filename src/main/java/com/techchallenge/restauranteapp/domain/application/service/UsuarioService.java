package com.techchallenge.restauranteapp.domain.application.service;

import com.techchallenge.restauranteapp.domain.exception.RecursoNaoEncontradoException;
import com.techchallenge.restauranteapp.domain.model.Usuario;
import com.techchallenge.restauranteapp.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public Usuario criar(Usuario u) {
        return repo.salvar(u);
    }

    public List<Usuario> listar() {
        return repo.listarTodos();
    }

    public Usuario buscar(Long id) {
        return repo.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    public Usuario atualizar(Long id, Usuario u) {
        buscar(id);
        u.setId(id);
        return repo.salvar(u);
    }

    @Transactional
    public Usuario deletarERetornar(Long id) {
        Usuario existente = buscar(id); 
        repo.deletar(id);
        return existente;
    }
}
