package com.techchallenge.restauranteapp.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.techchallenge.restauranteapp.domain.exception.RecursoNaoEncontradoException;
import com.techchallenge.restauranteapp.domain.model.Restaurante;
import com.techchallenge.restauranteapp.domain.model.entity.RestauranteEntity;
import com.techchallenge.restauranteapp.domain.model.entity.UsuarioEntity;
import com.techchallenge.restauranteapp.domain.repository.RestauranteJpaRepository;
import com.techchallenge.restauranteapp.domain.repository.RestauranteRepository;
import com.techchallenge.restauranteapp.domain.repository.UsuarioJpaRepository;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepository {

    private final RestauranteJpaRepository jpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public RestauranteRepositoryImpl(RestauranteJpaRepository jpaRepository,
                                     UsuarioJpaRepository usuarioJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public Restaurante salvar(Restaurante r) {

UsuarioEntity dono = usuarioJpaRepository.findById(r.getDonoUsuarioId())
    .orElseThrow(() -> new RecursoNaoEncontradoException(
        "Usuário (dono) %d não encontrado".formatted(r.getDonoUsuarioId())
    ));

        RestauranteEntity entity = RestauranteEntity.builder()
            .id(r.getId())
            .nome(r.getNome())
            .cnpj(r.getCnpj())
            .endereco(r.getEndereco())
            .tipoCozinha(r.getTipoCozinha())
            .horarioFuncionamento(r.getHorarioFuncionamento())
            .dono(dono)
            .build();

        RestauranteEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Restaurante> listarTodos() {
        return jpaRepository.findAll()
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Restaurante> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void deletar(Long id) {
        jpaRepository.deleteById(id);
    }

    // ===== Mapper =====
    private Restaurante toDomain(RestauranteEntity e) {
        return Restaurante.builder()
            .id(e.getId())
            .nome(e.getNome())
            .cnpj(e.getCnpj())
            .endereco(e.getEndereco())
            .tipoCozinha(e.getTipoCozinha())
            .horarioFuncionamento(e.getHorarioFuncionamento())
            .donoUsuarioId(e.getDono().getId())
            .build();
    }
}
