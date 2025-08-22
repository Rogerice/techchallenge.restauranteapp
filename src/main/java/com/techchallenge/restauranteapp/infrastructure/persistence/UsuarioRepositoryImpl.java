package com.techchallenge.restauranteapp.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.techchallenge.restauranteapp.domain.model.Usuario;
import com.techchallenge.restauranteapp.domain.model.entity.UsuarioEntity;
import com.techchallenge.restauranteapp.domain.repository.UsuarioJpaRepository;
import com.techchallenge.restauranteapp.domain.repository.UsuarioRepository;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioJpaRepository jpa;

    public UsuarioRepositoryImpl(UsuarioJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Usuario salvar(Usuario u) {
        UsuarioEntity e = toEntity(u);
        UsuarioEntity saved = jpa.save(e);
        return toDomain(saved);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public List<Usuario> listarTodos() {
        return jpa.findAll()
                  .stream()
                  .map(this::toDomain)
                  .collect(Collectors.toList());
    }

    @Override
    public void deletar(Long id) {
        jpa.deleteById(id);
    }

    // ===== mapeamentos =====

    private Usuario toDomain(UsuarioEntity e) {
        return Usuario.builder()
                .id(e.getId())
                .nome(e.getNome())
                .email(e.getEmail())
                .senha(e.getSenha())
                .tipoUsuario(e.getTipoUsuario()) // ENUM direto
                .build();
    }

    private UsuarioEntity toEntity(Usuario d) {
        return UsuarioEntity.builder()
                .id(d.getId())
                .nome(d.getNome())
                .email(d.getEmail())
                .senha(d.getSenha())
                .tipoUsuario(d.getTipoUsuario()) // ENUM direto
                .build();
    }
}
