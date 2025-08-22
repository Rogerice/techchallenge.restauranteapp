package com.techchallenge.restauranteapp.domain.application.service;

import com.techchallenge.restauranteapp.domain.exception.RecursoNaoEncontradoException;
import com.techchallenge.restauranteapp.domain.model.TipoUsuario;
import com.techchallenge.restauranteapp.domain.repository.TipoUsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TipoUsuarioServiceTest {

    private TipoUsuarioRepository repository;
    private TipoUsuarioService service;
    private TipoUsuario tipo;

    @BeforeEach
    void setUp() {
        repository = mock(TipoUsuarioRepository.class);
        service = new TipoUsuarioService(repository);

        tipo = TipoUsuario.builder()
                .id(1L)
                .nomeTipo("ADMIN")
                .build();
    }

    @Test
    void deveCriarTipoUsuarioComSucesso() {
        when(repository.salvar(any())).thenReturn(tipo);

        TipoUsuario resultado = service.criar(tipo);

        assertNotNull(resultado);
        assertEquals("ADMIN", resultado.getNomeTipo());
        verify(repository).salvar(tipo);
    }

    @Test
    void deveListarTodosOsTiposUsuarios() {
        when(repository.listarTodos()).thenReturn(Arrays.asList(tipo));

        List<TipoUsuario> lista = service.listarTodos();

        assertFalse(lista.isEmpty());
        verify(repository).listarTodos();
    }

    @Test
    void deveBuscarTipoUsuarioPorIdComSucesso() {
        when(repository.buscarPorId(1L)).thenReturn(Optional.of(tipo));

        TipoUsuario resultado = service.buscarPorIdOuFalhar(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repository).buscarPorId(1L);
    }

    @Test
    void deveLancarExcecaoAoBuscarTipoUsuarioInexistente() {
        when(repository.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorIdOuFalhar(1L));
    }

    @Test
    void deveDeletarTipoUsuarioComSucesso() {
        when(repository.buscarPorId(1L)).thenReturn(Optional.of(tipo));

        assertDoesNotThrow(() -> service.deletar(1L));
        verify(repository).deletar(1L);
    }

    @Test
    void deveLancarExcecaoAoDeletarTipoUsuarioInexistente() {
        when(repository.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.deletar(1L));
    }

    @Test
    void deveAtualizarTipoUsuarioComSucesso() {
        TipoUsuario atualizado = TipoUsuario.builder()
                .nomeTipo("CLIENTE")
                .build();

        when(repository.buscarPorId(1L)).thenReturn(Optional.of(tipo));
        when(repository.salvar(any())).thenReturn(tipo);

        TipoUsuario resultado = service.atualizar(1L, atualizado);

        assertNotNull(resultado);
        assertEquals("CLIENTE", resultado.getNomeTipo());
        verify(repository).salvar(tipo);
    }

    @Test
    void deveLancarExcecaoAoAtualizarTipoUsuarioInexistente() {
        when(repository.buscarPorId(1L)).thenReturn(Optional.empty());

        TipoUsuario atualizado = TipoUsuario.builder().nomeTipo("CLIENTE").build();

        assertThrows(RecursoNaoEncontradoException.class, () -> service.atualizar(1L, atualizado));
    }
}
