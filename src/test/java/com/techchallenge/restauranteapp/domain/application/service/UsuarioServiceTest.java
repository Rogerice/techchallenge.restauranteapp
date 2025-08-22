package com.techchallenge.restauranteapp.domain.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.techchallenge.restauranteapp.domain.exception.RecursoNaoEncontradoException;
import com.techchallenge.restauranteapp.domain.model.Usuario;
import com.techchallenge.restauranteapp.domain.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repo;

    @InjectMocks
    private UsuarioService service;

    private Usuario novoUsuario(Long id, String nome, String email) {
        // ajuste aqui se não usar builder()
        return Usuario.builder()
                .id(id)
                .nome(nome)
                .email(email)
                .build();
    }

    @Nested
    class CriarEListar {

        @Test
        @DisplayName("criar() - delega ao repositório e retorna salvo")
        void criar() {
            Usuario entrada = novoUsuario(null, "Ana", "ana@ex.com");
            Usuario salvo   = novoUsuario(10L, "Ana", "ana@ex.com");

            when(repo.salvar(any(Usuario.class))).thenReturn(salvo);

            Usuario r = service.criar(entrada);

            assertEquals(10L, r.getId());
            assertEquals("Ana", r.getNome());
            verify(repo).salvar(entrada);
            verifyNoMoreInteractions(repo);
        }

        @Test
        @DisplayName("listar() - delega ao repositório")
        void listar() {
            when(repo.listarTodos()).thenReturn(List.of());
            assertNotNull(service.listar());
            verify(repo).listarTodos();
            verifyNoMoreInteractions(repo);
        }
    }

    @Nested
    class Buscar {

        @Test
        @DisplayName("buscar() - encontrado")
        void buscar_ok() {
            when(repo.buscarPorId(5L)).thenReturn(Optional.of(novoUsuario(5L, "Bob", "b@ex.com")));

            Usuario u = service.buscar(5L);

            assertEquals(5L, u.getId());
            assertEquals("Bob", u.getNome());
            verify(repo).buscarPorId(5L);
            verifyNoMoreInteractions(repo);
        }

        @Test
        @DisplayName("buscar() - não encontrado lança RecursoNaoEncontradoException")
        void buscar_naoEncontrado() {
            when(repo.buscarPorId(7L)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () -> service.buscar(7L));

            verify(repo).buscarPorId(7L);
            verifyNoMoreInteractions(repo);
        }
    }

    @Nested
    class AtualizarEDeletar {

        @Test
        @DisplayName("atualizar() - garante existência, seta id e salva")
        void atualizar() {
            Usuario existente = novoUsuario(3L, "Carol", "c@ex.com");
            Usuario entrada   = novoUsuario(null, "Carol 2", "c2@ex.com");
            Usuario salvo     = novoUsuario(3L, "Carol 2", "c2@ex.com");

            when(repo.buscarPorId(3L)).thenReturn(Optional.of(existente));
            when(repo.salvar(any(Usuario.class))).thenReturn(salvo);

            Usuario r = service.atualizar(3L, entrada);

            assertEquals(3L, r.getId());
            assertEquals("Carol 2", r.getNome());
            verify(repo).buscarPorId(3L);
            // garante que o id foi setado antes de salvar
            verify(repo).salvar(argThat(u -> u.getId().equals(3L) &&
                                             "Carol 2".equals(u.getNome()) &&
                                             "c2@ex.com".equals(u.getEmail())));
            verifyNoMoreInteractions(repo);
        }

        @Test
        @DisplayName("deletarERetornar() - remove e devolve o existente")
        void deletarERetornar() {
            Usuario existente = novoUsuario(9L, "Dan", "d@ex.com");
            when(repo.buscarPorId(9L)).thenReturn(Optional.of(existente));

            Usuario r = service.deletarERetornar(9L);

            assertEquals(9L, r.getId());
            assertEquals("Dan", r.getNome());
            verify(repo).buscarPorId(9L);
            verify(repo).deletar(9L);
            verifyNoMoreInteractions(repo);
        }
    }
}
