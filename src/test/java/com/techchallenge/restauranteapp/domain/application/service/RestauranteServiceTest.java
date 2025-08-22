package com.techchallenge.restauranteapp.domain.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.techchallenge.restauranteapp.domain.exception.RecursoNaoEncontradoException;
import com.techchallenge.restauranteapp.domain.model.Restaurante;
import com.techchallenge.restauranteapp.domain.repository.RestauranteRepository;

class RestauranteServiceTest {

    private RestauranteRepository repository;
    private RestauranteService service;

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        repository = mock(RestauranteRepository.class);
        service = new RestauranteService(repository);

        restaurante = Restaurante.builder()
                .id(1L)
                .nome("Adega do Zé")
                .cnpj("12345678900001")
                .build();
    }


    @Test
    void deveListarTodosOsRestaurantes() {
        when(repository.listarTodos()).thenReturn(Arrays.asList(restaurante));

        List<Restaurante> lista = service.listarTodos();

        assertFalse(lista.isEmpty());
        verify(repository).listarTodos();
    }

    @Test
    void deveBuscarRestaurantePorIdExistente() {
        when(repository.buscarPorId(1L)).thenReturn(Optional.of(restaurante));

        Restaurante resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repository).buscarPorId(1L);
    }

    @Test
    void deveLancarExcecaoAoBuscarRestaurantePorIdInexistente() {
        when(repository.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(1L));
        verify(repository).buscarPorId(1L);
    }

    @Test
    void deveAtualizarRestauranteComSucesso() {
        Restaurante atualizado = Restaurante.builder()
                .nome("Adega Atualizada")
                .cnpj("98765432100001")
                .build();

        when(repository.buscarPorId(1L)).thenReturn(Optional.of(restaurante));
        when(repository.salvar(any())).thenReturn(restaurante);

        Restaurante resultado = service.atualizar(1L, atualizado);

        assertNotNull(resultado);
        verify(repository).salvar(restaurante);
    }

    @Test
    void deveLancarExcecaoAoAtualizarRestauranteInexistente() {
        when(repository.buscarPorId(1L)).thenReturn(Optional.empty());

        Restaurante atualizado = Restaurante.builder()
                .nome("Novo")
                .cnpj("00000000000100")
                .build();

        assertThrows(RecursoNaoEncontradoException.class, () -> service.atualizar(1L, atualizado));
    }

    @Test
    void deveDeletarRestauranteComSucesso() {
        when(repository.buscarPorId(1L)).thenReturn(Optional.of(restaurante));

        assertDoesNotThrow(() -> service.deletar(1L));
        verify(repository).deletar(1L);
    }

    @Test
    void deveLancarExcecaoAoDeletarRestauranteInexistente() {
        when(repository.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.deletar(1L));
    }
}
