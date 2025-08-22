package com.techchallenge.restauranteapp.domain.application.service;

import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techchallenge.restauranteapp.domain.exception.RecursoNaoEncontradoException;
import com.techchallenge.restauranteapp.domain.exception.ValidacaoException;
import com.techchallenge.restauranteapp.domain.exception.ViolacaoDeRegraNegocioException;
import com.techchallenge.restauranteapp.domain.model.ItemCardapio;
import com.techchallenge.restauranteapp.domain.model.Restaurante;
import com.techchallenge.restauranteapp.domain.repository.ItemCardapioRepository;
import com.techchallenge.restauranteapp.domain.repository.RestauranteRepository;

@Service
public class ItemCardapioService {

    private final ItemCardapioRepository repository;
    private final RestauranteRepository restauranteRepository;

    public ItemCardapioService(ItemCardapioRepository repository,
                               RestauranteRepository restauranteRepository) {
        this.repository = repository;
        this.restauranteRepository = restauranteRepository;
    }

    @Transactional
    public ItemCardapio criar(Long restauranteId, ItemCardapio item) {
        validar(item);
        verificarRestaurante(restauranteId);
        validarUnicidade(restauranteId, item.getNome());

        normalizar(item);
        item.setRestaurante(Restaurante.builder().id(restauranteId).build());

        return repository.salvar(item);
    }

    public List<ItemCardapio> listarTodos() {
        return repository.listarTodos();
    }

    public ItemCardapio buscarPorId(Long id) {
        return repository.buscarPorId(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Item de cardápio não encontrado"));
    }

    @Transactional
    public ItemCardapio atualizar(Long id, ItemCardapio itemAtualizado) {
        ItemCardapio existente = buscarPorId(id);
        validar(itemAtualizado);

        // mantém o restaurante original
        itemAtualizado.setRestaurante(existente.getRestaurante());

        Long restauranteId = existente.getRestaurante().getId();
        if (!existente.getNome().equalsIgnoreCase(itemAtualizado.getNome())) {
            validarUnicidade(restauranteId, itemAtualizado.getNome());
        }

        normalizar(itemAtualizado);

        existente.setNome(itemAtualizado.getNome());
        existente.setDescricao(itemAtualizado.getDescricao());
        existente.setPreco(itemAtualizado.getPreco());
        existente.setDisponivel(itemAtualizado.getDisponivel());
        existente.setCaminhoFoto(itemAtualizado.getCaminhoFoto());
        existente.setSomenteNoLocal(itemAtualizado.getSomenteNoLocal());

        return repository.salvar(existente);
    }

    @Transactional
    public ItemCardapio deletarERetornar(Long id) {
        ItemCardapio existente = buscarPorId(id);
        repository.deletar(id);
        return existente;
    }

    public List<ItemCardapio> listarPorRestaurante(Long restauranteId) {
        return repository.listarPorRestaurante(restauranteId);
    }

    private void validar(ItemCardapio i) {
        if (i.getNome() == null || i.getNome().isBlank())
            throw new ValidacaoException("nome é obrigatório");
        if (i.getPreco() == null)
            throw new ValidacaoException("preco é obrigatório");
        if (i.getPreco().signum() <= 0)
            throw new ValidacaoException("preco deve ser maior que zero");
        if (i.getDisponivel() == null) i.setDisponivel(true);
        if (i.getSomenteNoLocal() == null) i.setSomenteNoLocal(false);
    }

    private void normalizar(ItemCardapio i) {
        i.setPreco(i.getPreco().setScale(2, RoundingMode.HALF_UP));
        if (i.getNome() != null) i.setNome(i.getNome().trim());
        if (i.getDescricao() != null) i.setDescricao(i.getDescricao().trim());
    }

    private void verificarRestaurante(Long restauranteId) {
        if (restauranteRepository.buscarPorId(restauranteId).isEmpty()) {
            throw new RecursoNaoEncontradoException("Restaurante " + restauranteId + " não encontrado");
        }
    }

    private void validarUnicidade(Long restauranteId, String nome) {
        if (repository.existePorRestauranteENome(restauranteId, nome)) {
            throw new ViolacaoDeRegraNegocioException(
                "Já existe item com nome '" + nome + "' neste restaurante");
        }
    }
}
