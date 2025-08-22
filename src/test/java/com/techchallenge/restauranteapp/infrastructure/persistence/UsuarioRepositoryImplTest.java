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

import com.techchallenge.restauranteapp.domain.enums.TipoUsuarioEnum;
import com.techchallenge.restauranteapp.domain.model.Usuario;
import com.techchallenge.restauranteapp.domain.model.entity.UsuarioEntity;
import com.techchallenge.restauranteapp.domain.repository.UsuarioJpaRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryImplTest {

	@Mock
	private UsuarioJpaRepository jpa;

	@InjectMocks
	private UsuarioRepositoryImpl repo;

	private Usuario domain(Long id, String nome, String email, String senha, TipoUsuarioEnum tipo) {
		return Usuario.builder().id(id).nome(nome).email(email).senha(senha).tipoUsuario(tipo).build();
	}

	private UsuarioEntity entity(Long id, String nome, String email, String senha, TipoUsuarioEnum tipo) {
		return UsuarioEntity.builder().id(id).nome(nome).email(email).senha(senha).tipoUsuario(tipo).build();
	}

	@Nested
	class Salvar {

		@Test
		@DisplayName("salvar() → envia entity ao JPA e retorna domínio mapeado")
		void salvar_ok() {
			var entrada = domain(null, "Ana", "ana@ex.com", "123", TipoUsuarioEnum.CLIENTE);
			var salvoEntity = entity(10L, "Ana", "ana@ex.com", "123", TipoUsuarioEnum.CLIENTE);

			when(jpa.save(any(UsuarioEntity.class))).thenReturn(salvoEntity);

			var out = repo.salvar(entrada);

			assertEquals(10L, out.getId());
			assertEquals("Ana", out.getNome());
			assertEquals("ana@ex.com", out.getEmail());
			assertEquals("123", out.getSenha());
			assertEquals(TipoUsuarioEnum.CLIENTE, out.getTipoUsuario());

			ArgumentCaptor<UsuarioEntity> cap = ArgumentCaptor.forClass(UsuarioEntity.class);
			verify(jpa).save(cap.capture());
			var enviado = cap.getValue();
			assertNull(enviado.getId());
			assertEquals("Ana", enviado.getNome());
			assertEquals("ana@ex.com", enviado.getEmail());
			assertEquals("123", enviado.getSenha());
			assertEquals(TipoUsuarioEnum.CLIENTE, enviado.getTipoUsuario());
		}
	}

	@Nested
	class BuscarEListar {

		@Test
		@DisplayName("buscarPorId() → presente retorna domínio; ausente Optional.empty()")
		void buscarPorId() {
			when(jpa.findById(5L)).thenReturn(Optional.of(entity(5L, "Bob", "b@ex.com", "x", TipoUsuarioEnum.DONO)));
			when(jpa.findById(6L)).thenReturn(Optional.empty());

			var ok = repo.buscarPorId(5L);
			var empty = repo.buscarPorId(6L);

			assertTrue(ok.isPresent());
			assertEquals(5L, ok.get().getId());
			assertEquals("Bob", ok.get().getNome());
			assertEquals(TipoUsuarioEnum.DONO, ok.get().getTipoUsuario());
			assertTrue(empty.isEmpty());

			verify(jpa).findById(5L);
			verify(jpa).findById(6L);
		}

		@Test
		@DisplayName("listarTodos() → mapeia lista de entities para domínios")
		void listarTodos() {
			when(jpa.findAll()).thenReturn(List.of(entity(1L, "Rafa", "r@ex.com", "a", TipoUsuarioEnum.CLIENTE),
					entity(2L, "Bia", "b@ex.com", "b", TipoUsuarioEnum.ADMIN)));

			var lista = repo.listarTodos();

			assertEquals(2, lista.size());
			assertEquals("Rafa", lista.get(0).getNome());
			assertEquals(TipoUsuarioEnum.CLIENTE, lista.get(0).getTipoUsuario());
			assertEquals("Bia", lista.get(1).getNome());
			assertEquals(TipoUsuarioEnum.ADMIN, lista.get(1).getTipoUsuario());
			verify(jpa).findAll();
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
