package com.techchallenge.restauranteapp.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.techchallenge.restauranteapp.domain.exception.RecursoNaoEncontradoException;
import com.techchallenge.restauranteapp.domain.model.Restaurante;
import com.techchallenge.restauranteapp.domain.model.entity.RestauranteEntity;
import com.techchallenge.restauranteapp.domain.model.entity.UsuarioEntity;
import com.techchallenge.restauranteapp.domain.repository.RestauranteJpaRepository;
import com.techchallenge.restauranteapp.domain.repository.UsuarioJpaRepository;

@ExtendWith(MockitoExtension.class)
class RestauranteRepositoryImplTest {

    @Mock
    private RestauranteJpaRepository restJpa;

    @Mock
    private UsuarioJpaRepository usuarioJpa;

    @InjectMocks
    private RestauranteRepositoryImpl repo;

    // ===== Helpers =====
    private UsuarioEntity donoEntity(Long id, String nome, String email) {
        // ajuste para setters se sua entity não tiver @Builder
        return UsuarioEntity.builder()
                .id(id)
                .nome(nome)
                .email(email)
                .senha("x")
                .build();
    }

    private RestauranteEntity restEntity(Long id, Long donoId, String nome) {
        return RestauranteEntity.builder()
                .id(id)
                .nome(nome)
                .cnpj("12345678000190")
                .endereco("Rua X, 123")
                .tipoCozinha("BRASILEIRA")
                .horarioFuncionamento("08:00-18:00")
                .dono(donoEntity(donoId, "Dono", "d@ex.com"))
                .build();
    }

    private Restaurante domainRest(Long id, Long donoUsuarioId, String nome) {
        // ajuste para setters se seu domínio não tiver @Builder
        return Restaurante.builder()
                .id(id)
                .nome(nome)
                .cnpj("12345678000190")
                .endereco("Rua X, 123")
                .tipoCozinha("BRASILEIRA")
                .horarioFuncionamento("08:00-18:00")
                .donoUsuarioId(donoUsuarioId)
                .build();
    }

    @Nested
    class Salvar {

        @Test
        @DisplayName("salvar() → carrega dono, persiste entity e retorna domínio mapeado")
        void salvar_ok() {
            var dono = donoEntity(7L, "Alice", "a@ex.com");
            var salvo = restEntity(10L, 7L, "Casa da Esfiha");

            when(usuarioJpa.findById(7L)).thenReturn(Optional.of(dono));
            when(restJpa.save(any(RestauranteEntity.class))).thenReturn(salvo);

            var entrada = domainRest(null, 7L, "Casa da Esfiha");

            Restaurante out = repo.salvar(entrada);

            // retorno mapeado
            assertNotNull(out.getId());
            assertEquals(10L, out.getId());
            assertEquals("Casa da Esfiha", out.getNome());
            assertEquals("BRASILEIRA", out.getTipoCozinha());
            assertEquals(7L, out.getDonoUsuarioId());

            // verifica entity enviada ao JPA
            ArgumentCaptor<RestauranteEntity> cap = ArgumentCaptor.forClass(RestauranteEntity.class);
            verify(restJpa).save(cap.capture());
            var enviado = cap.getValue();
            assertEquals("Casa da Esfiha", enviado.getNome());
            assertEquals("12345678000190", enviado.getCnpj());
            assertEquals("Rua X, 123", enviado.getEndereco());
            assertEquals("BRASILEIRA", enviado.getTipoCozinha());
            assertEquals("08:00-18:00", enviado.getHorarioFuncionamento());
            assertNotNull(enviado.getDono());
            assertEquals(7L, enviado.getDono().getId());

            verify(usuarioJpa).findById(7L);
        }

        @Test
        @DisplayName("salvar() → lança 404 se dono (usuario) não existir")
        void salvar_donoInexistente() {
            when(usuarioJpa.findById(999L)).thenReturn(Optional.empty());

            var entrada = domainRest(null, 999L, "R1");

            RecursoNaoEncontradoException ex =
                    assertThrows(RecursoNaoEncontradoException.class, () -> repo.salvar(entrada));

            assertTrue(ex.getMessage().contains("Usuário (dono) 999 não encontrado"));
            verify(usuarioJpa).findById(999L);
            verify(restJpa, never()).save(any());
        }
    }

    @Nested
    class LeituraEConsulta {

        @Test
        @DisplayName("listarTodos() → mapeia lista de entities para domínios")
        void listarTodos() {
            var e1 = restEntity(1L, 5L, "R1");
            var e2 = restEntity(2L, 6L, "R2");
            when(restJpa.findAll()).thenReturn(List.of(e1, e2));

            List<Restaurante> lista = repo.listarTodos();

            assertEquals(2, lista.size());
            assertEquals("R1", lista.get(0).getNome());
            assertEquals(5L, lista.get(0).getDonoUsuarioId());
            assertEquals("R2", lista.get(1).getNome());
            assertEquals(6L, lista.get(1).getDonoUsuarioId());
            verify(restJpa).findAll();
        }

        @Test
        @DisplayName("buscarPorId() → presente retorna domain, ausente vazio")
        void buscarPorId() {
            when(restJpa.findById(10L)).thenReturn(Optional.of(restEntity(10L, 7L, "R10")));
            when(restJpa.findById(11L)).thenReturn(Optional.empty());

            assertTrue(repo.buscarPorId(10L).isPresent());
            assertTrue(repo.buscarPorId(11L).isEmpty());

            verify(restJpa).findById(10L);
            verify(restJpa).findById(11L);
        }
    }

    @Nested
    class Deletar {

        @Test
        @DisplayName("deletar(id) → delega ao jpa.deleteById")
        void deletar() {
            doNothing().when(restJpa).deleteById(3L);
            repo.deletar(3L);
            verify(restJpa).deleteById(3L);
        }
    }
}
