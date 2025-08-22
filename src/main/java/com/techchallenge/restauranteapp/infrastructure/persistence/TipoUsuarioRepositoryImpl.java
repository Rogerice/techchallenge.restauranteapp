package com.techchallenge.restauranteapp.infrastructure.persistence;

import com.techchallenge.restauranteapp.domain.model.TipoUsuario;
import com.techchallenge.restauranteapp.domain.model.entity.TipoUsuarioEntity;
import com.techchallenge.restauranteapp.domain.repository.TipoUsuarioJpaRepository;
import com.techchallenge.restauranteapp.domain.repository.TipoUsuarioRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TipoUsuarioRepositoryImpl implements TipoUsuarioRepository {

    private final TipoUsuarioJpaRepository jpaRepository;

    public TipoUsuarioRepositoryImpl(TipoUsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public TipoUsuario salvar(TipoUsuario tipoUsuario) {
        TipoUsuarioEntity entity = toEntity(tipoUsuario);
        TipoUsuarioEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<TipoUsuario> listarTodos() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TipoUsuario> buscarPorId(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void deletar(Long id) {
        jpaRepository.deleteById(id);
    }

    private TipoUsuarioEntity toEntity(TipoUsuario domain) {
        return TipoUsuarioEntity.builder()
                .id(domain.getId())
                .nomeTipo(domain.getNomeTipo())
                .build();
    }

    private TipoUsuario toDomain(TipoUsuarioEntity entity) {
        return TipoUsuario.builder()
                .id(entity.getId())
                .nomeTipo(entity.getNomeTipo())
                .build();
    }
    
}
