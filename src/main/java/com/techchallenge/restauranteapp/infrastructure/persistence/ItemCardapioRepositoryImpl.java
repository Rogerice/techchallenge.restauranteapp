package com.techchallenge.restauranteapp.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.techchallenge.restauranteapp.domain.exception.RecursoNaoEncontradoException;
import com.techchallenge.restauranteapp.domain.model.ItemCardapio;
import com.techchallenge.restauranteapp.domain.model.Restaurante;
import com.techchallenge.restauranteapp.domain.model.entity.ItemCardapioEntity;
import com.techchallenge.restauranteapp.domain.model.entity.RestauranteEntity;
import com.techchallenge.restauranteapp.domain.repository.ItemCardapioJpaRepository;
import com.techchallenge.restauranteapp.domain.repository.ItemCardapioRepository;
import com.techchallenge.restauranteapp.domain.repository.RestauranteJpaRepository;

@Repository
public class ItemCardapioRepositoryImpl implements ItemCardapioRepository {

    private final ItemCardapioJpaRepository jpaRepository;
    private final RestauranteJpaRepository restauranteJpaRepository;

    public ItemCardapioRepositoryImpl(ItemCardapioJpaRepository jpaRepository,
                                      RestauranteJpaRepository restauranteJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.restauranteJpaRepository = restauranteJpaRepository;
    }

    @Override
    public ItemCardapio salvar(ItemCardapio d) {
        // Agora o restaurante vem dentro do objeto (apenas com id)
        if (d.getRestaurante() == null || d.getRestaurante().getId() == null) {
            throw new RecursoNaoEncontradoException("Restaurante não informado");
        }

        Long restauranteId = d.getRestaurante().getId();

        RestauranteEntity rest = restauranteJpaRepository.findById(restauranteId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante " + restauranteId + " não encontrado"));

        Boolean disponivel = d.getDisponivel() != null ? d.getDisponivel() : Boolean.TRUE;
        Boolean somenteNoLocal = d.getSomenteNoLocal() != null ? d.getSomenteNoLocal() : Boolean.FALSE;

        ItemCardapioEntity e = ItemCardapioEntity.builder()
            .id(d.getId())
            .nome(d.getNome())
            .descricao(d.getDescricao())
            .preco(d.getPreco())
            .disponivel(disponivel)
            .caminhoFoto(d.getCaminhoFoto())
            .somenteNoLocal(somenteNoLocal)
            .restaurante(rest) // associa a entidade carregada
            .build();

        return toDomain(jpaRepository.save(e));
    }

    @Override
    public List<ItemCardapio> listarTodos() {
        return jpaRepository.findAll()
            .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<ItemCardapio> buscarPorId(Long id) {
        return jpaRepository.findOneById(id).map(this::toDomain);
    }

    @Override
    public List<ItemCardapio> listarPorRestaurante(Long restauranteId) {
        return jpaRepository.findAllByRestaurante_Id(restauranteId)
            .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deletar(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorRestauranteENome(Long restauranteId, String nome) {
        return jpaRepository.existsByRestaurante_IdAndNomeIgnoreCase(restauranteId, nome);
    }

    private ItemCardapio toDomain(ItemCardapioEntity e) {
        var re = e.getRestaurante();

        // Se tiver relacionamento para o dono (ex.: re.getDonoUsuario()), ajuste abaixo conforme seu modelo:
        Long donoUsuarioId = null;
        try {
            // só se existir esse relacionamento no seu entity:
            donoUsuarioId = (re.getDono() != null ? re.getDono().getId() : null);
        } catch (Exception ignore) {}

        return ItemCardapio.builder()
            .id(e.getId())
            .nome(e.getNome())
            .descricao(e.getDescricao())
            .preco(e.getPreco())
            .disponivel(e.getDisponivel())
            .caminhoFoto(e.getCaminhoFoto())
            .somenteNoLocal(e.getSomenteNoLocal())
            .restaurante(Restaurante.builder()
                .id(re.getId())
                .nome(re.getNome())
                .cnpj(re.getCnpj())
                .endereco(re.getEndereco())
                .tipoCozinha(re.getTipoCozinha())
                .horarioFuncionamento(re.getHorarioFuncionamento())
                // só inclua se existir esse campo no seu domínio:
                .donoUsuarioId(donoUsuarioId)
                .build())
            .build();
    }
}
