package com.techchallenge.restauranteapp.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

import com.techchallenge.restauranteapp.domain.model.TipoUsuario;
import com.techchallenge.restauranteapp.domain.model.entity.TipoUsuarioEntity;
import com.techchallenge.restauranteapp.domain.repository.TipoUsuarioJpaRepository;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioRepositoryImplTest {

    @Mock
    private TipoUsuarioJpaRepository jpa;

    @InjectMocks
    private TipoUsuarioRepositoryImpl repo;

    // ===== Helpers =====
    private TipoUsuario domain(Long id, String nome) {
        // troque por setters se não tiver @Builder
        return TipoUsuario.builder()
                .id(id)
                .nomeTipo(nome)
                .build();
    }

    private TipoUsuarioEntity entity(Long id, String nome) {
        // troque por setters se não tiver @Builder
        return TipoUsuarioEntity.builder()
                .id(id)
                .nomeTipo(nome)
                .build();
    }

    @Nested
    class Salvar {

        @Test
        @DisplayName("salvar() → envia entity ao JPA e retorna domínio mapeado")
        void salvar_ok() {
            var entrada = domain(null, "GERENTE");
            var salvoEntity = entity(10L, "GERENTE");

            when(jpa.save(any(TipoUsuarioEntity.class))).thenReturn(salvoEntity);

            var out = repo.salvar(entrada);

            assertNotNull(out.getId());
            assertEquals(10L, out.getId());
            assertEquals("GERENTE", out.getNomeTipo());

            // captura o argumento enviado ao jpa.save
            ArgumentCaptor<TipoUsuarioEntity> cap = ArgumentCaptor.forClass(TipoUsuarioEntity.class);
            verify(jpa).save(cap.capture());
            assertNull(cap.getValue().getId());         // id nulo ao criar
            assertEquals("GERENTE", cap.getValue().getNomeTipo());
        }
    }

    @Nested
    class LeituraEConsulta {

        @Test
        @DisplayName("listarTodos() → mapeia lista de entities para domínios")
        void listarTodos() {
            when(jpa.findAll()).thenReturn(List.of(
                    entity(1L, "CLIENTE"),
                    entity(2L, "DONO")
            ));

            var lista = repo.listarTodos();

            assertEquals(2, lista.size());
            assertEquals("CLIENTE", lista.get(0).getNomeTipo());
            assertEquals("DONO", lista.get(1).getNomeTipo());
            verify(jpa).findAll();
        }

        @Test
        @DisplayName("buscarPorId() → presente retorna domain; ausente retorna Optional.empty()")
        void buscarPorId() {
            when(jpa.findById(5L)).thenReturn(Optional.of(entity(5L, "ADMIN")));
            when(jpa.findById(6L)).thenReturn(Optional.empty());

            var ok = repo.buscarPorId(5L);
            var empty = repo.buscarPorId(6L);

            assertTrue(ok.isPresent());
            assertEquals(5L, ok.get().getId());
            assertEquals("ADMIN", ok.get().getNomeTipo());
            assertTrue(empty.isEmpty());

            verify(jpa).findById(5L);
            verify(jpa).findById(6L);
        }
    }

    @Nested
    class Deletar {

        @Test
        @DisplayName("deletar(id) → delega ao jpa.deleteById")
        void deletar() {
            doNothing().when(jpa).deleteById(9L);

            repo.deletar(9L);

            verify(jpa).deleteById(9L);
            verifyNoMoreInteractions(jpa);
        }
    }
}
