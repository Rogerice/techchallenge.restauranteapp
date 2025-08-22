package com.techchallenge.restauranteapp.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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
import com.techchallenge.restauranteapp.domain.model.ItemCardapio;
import com.techchallenge.restauranteapp.domain.model.Restaurante;
import com.techchallenge.restauranteapp.domain.model.entity.ItemCardapioEntity;
import com.techchallenge.restauranteapp.domain.model.entity.RestauranteEntity;
import com.techchallenge.restauranteapp.domain.repository.ItemCardapioJpaRepository;
import com.techchallenge.restauranteapp.domain.repository.RestauranteJpaRepository;

@ExtendWith(MockitoExtension.class)
class ItemCardapioRepositoryImplTest {

    @Mock
    private ItemCardapioJpaRepository itemJpa;

    @Mock
    private RestauranteJpaRepository restJpa;

    @InjectMocks
    private ItemCardapioRepositoryImpl repo;

    // ===== Helpers =====
    private RestauranteEntity restEntity(Long id, String nome) {
        // Ajuste para setters se seu entity não tiver @Builder
        return RestauranteEntity.builder()
                .id(id)
                .nome(nome)
                .cnpj("12345678000190")
                .endereco("Rua X, 123")
                .horarioFuncionamento("08:00-18:00")
                .tipoCozinha("BRASILEIRA")
                .build();
    }

    private ItemCardapioEntity itemEntity(Long id, RestauranteEntity r, String nome, String preco,
                                          Boolean disponivel, Boolean somenteNoLocal) {
        return ItemCardapioEntity.builder()
                .id(id)
                .nome(nome)
                .descricao("desc e")
                .preco(new BigDecimal(preco))
                .disponivel(disponivel)
                .somenteNoLocal(somenteNoLocal)
                .caminhoFoto("/img.png")
                .restaurante(r)
                .build();
    }

    private ItemCardapio domainItem(Long id, Long restId, String nome, String preco,
                                    Boolean disponivel, Boolean somenteNoLocal) {
        return ItemCardapio.builder()
                .id(id)
                .nome(nome)
                .descricao("desc d")
                .preco(new BigDecimal(preco))
                .disponivel(disponivel)
                .somenteNoLocal(somenteNoLocal)
                .caminhoFoto("/img.png")
                .restaurante(Restaurante.builder().id(restId).build())
                .build();
    }

    @Nested
    class Salvar {

        @Test
        @DisplayName("salvar() → falha se restaurante não vier no domínio")
        void salvar_semRestaurante() {
            ItemCardapio d = ItemCardapio.builder()
                    .nome("Burger")
                    .preco(new BigDecimal("10"))
                    .build();

            RecursoNaoEncontradoException ex = assertThrows(RecursoNaoEncontradoException.class,
                    () -> repo.salvar(d));

            assertTrue(ex.getMessage().contains("Restaurante não informado"));
            verifyNoInteractions(restJpa, itemJpa);
        }

        @Test
        @DisplayName("salvar() → falha se restaurante id não existir no JPA")
        void salvar_restauranteInexistente() {
            ItemCardapio d = domainItem(null, 77L, "Burger", "25.00", true, false);

            when(restJpa.findById(77L)).thenReturn(Optional.empty());

            RecursoNaoEncontradoException ex = assertThrows(RecursoNaoEncontradoException.class,
                    () -> repo.salvar(d));

            assertTrue(ex.getMessage().contains("Restaurante 77 não encontrado"));
            verify(restJpa).findById(77L);
            verify(itemJpa, never()).save(any());
        }

        @Test
        @DisplayName("salvar() → aplica defaults (disponivel=true, somenteNoLocal=false) quando nulos")
        void salvar_comDefaults() {
            var rest = restEntity(99L, "Casa");
            var salvo = itemEntity(10L, rest, "Burger", "25.00", true, false);

            when(restJpa.findById(99L)).thenReturn(Optional.of(rest));
            when(itemJpa.save(any(ItemCardapioEntity.class))).thenReturn(salvo);

            ItemCardapio entrada = domainItem(null, 99L, "Burger", "25.00", null, null);

            ItemCardapio out = repo.salvar(entrada);

            // valida retorno mapeado
            assertEquals(10L, out.getId());
            assertEquals("Burger", out.getNome());
            assertEquals(new BigDecimal("25.00"), out.getPreco());
            assertTrue(out.getDisponivel());
            assertFalse(out.getSomenteNoLocal());
            assertNotNull(out.getRestaurante());
            assertEquals(99L, out.getRestaurante().getId());
            assertEquals("Casa", out.getRestaurante().getNome());

            // captura entidade salva para inspecionar defaults aplicados
            ArgumentCaptor<ItemCardapioEntity> cap = ArgumentCaptor.forClass(ItemCardapioEntity.class);
            verify(itemJpa).save(cap.capture());
            ItemCardapioEntity enviado = cap.getValue();
            assertTrue(Boolean.TRUE.equals(enviado.getDisponivel()));
            assertTrue(Boolean.FALSE.equals(enviado.getSomenteNoLocal()));

            verify(restJpa).findById(99L);
        }

        @Test
        @DisplayName("salvar() → mantém flags informadas e mapeia corretamente")
        void salvar_mapeamentoBasico() {
            var rest = restEntity(50L, "R50");
            var retorno = itemEntity(5L, rest, "Suco", "7.50", false, true);

            when(restJpa.findById(50L)).thenReturn(Optional.of(rest));
            when(itemJpa.save(any(ItemCardapioEntity.class))).thenReturn(retorno);

            ItemCardapio entrada = domainItem(null, 50L, "Suco", "7.50", false, true);

            ItemCardapio r = repo.salvar(entrada);

            assertEquals(5L, r.getId());
            assertEquals("Suco", r.getNome());
            assertEquals(new BigDecimal("7.50"), r.getPreco());
            assertFalse(r.getDisponivel());
            assertTrue(r.getSomenteNoLocal());
            assertEquals(50L, r.getRestaurante().getId());

            verify(restJpa).findById(50L);
            verify(itemJpa).save(any(ItemCardapioEntity.class));
        }
    }

    @Nested
    class LeituraEConsulta {

        @Test
        @DisplayName("listarTodos() → mapeia entidades para domínio")
        void listarTodos() {
            var r = restEntity(1L, "R1");
            var e1 = itemEntity(1L, r, "A", "10.00", true, false);
            var e2 = itemEntity(2L, r, "B", "20.00", false, true);

            when(itemJpa.findAll()).thenReturn(List.of(e1, e2));

            List<ItemCardapio> out = repo.listarTodos();

            assertEquals(2, out.size());
            assertEquals("A", out.get(0).getNome());
            assertEquals("B", out.get(1).getNome());
            assertEquals(1L, out.get(0).getRestaurante().getId());
            verify(itemJpa).findAll();
        }

        @Test
        @DisplayName("buscarPorId() → presente mapeado, ausente vazio")
        void buscarPorId() {
            var r = restEntity(2L, "R2");
            var e = itemEntity(7L, r, "C", "12.00", true, false);

            when(itemJpa.findOneById(7L)).thenReturn(Optional.of(e));
            when(itemJpa.findOneById(8L)).thenReturn(Optional.empty());

            assertTrue(repo.buscarPorId(7L).isPresent());
            assertTrue(repo.buscarPorId(8L).isEmpty());

            verify(itemJpa).findOneById(7L);
            verify(itemJpa).findOneById(8L);
        }

        @Test
        @DisplayName("listarPorRestaurante() → filtra e mapeia")
        void listarPorRestaurante() {
            var r = restEntity(99L, "R99");
            var e = itemEntity(3L, r, "D", "13.00", true, false);

            when(itemJpa.findAllByRestaurante_Id(99L)).thenReturn(List.of(e));

            var lista = repo.listarPorRestaurante(99L);

            assertEquals(1, lista.size());
            assertEquals("D", lista.get(0).getNome());
            assertEquals(99L, lista.get(0).getRestaurante().getId());
            verify(itemJpa).findAllByRestaurante_Id(99L);
        }
    }

    @Nested
    class OutrosMetodos {

        @Test
        @DisplayName("deletar(id) → delega ao JPA")
        void deletar() {
            doNothing().when(itemJpa).deleteById(11L);
            repo.deletar(11L);
            verify(itemJpa).deleteById(11L);
        }

        @Test
        @DisplayName("existePorRestauranteENome → delega ao existsByRestaurante_IdAndNomeIgnoreCase")
        void existePorRestauranteENome() {
            when(itemJpa.existsByRestaurante_IdAndNomeIgnoreCase(5L, "Pizza")).thenReturn(true);

            boolean r = repo.existePorRestauranteENome(5L, "Pizza");
            assertTrue(r);
            verify(itemJpa).existsByRestaurante_IdAndNomeIgnoreCase(5L, "Pizza");
        }
    }
}
