package com.techchallenge.restauranteapp.domain.repository;

import java.util.List;
import java.util.Optional;

import com.techchallenge.restauranteapp.domain.model.ItemCardapio;

public interface ItemCardapioRepository {
    ItemCardapio salvar(ItemCardapio item);

    List<ItemCardapio> listarTodos();

    Optional<ItemCardapio> buscarPorId(Long id);

    void deletar(Long id);

    List<ItemCardapio> listarPorRestaurante(Long restauranteId);

    boolean existePorRestauranteENome(Long restauranteId, String nome);
}
