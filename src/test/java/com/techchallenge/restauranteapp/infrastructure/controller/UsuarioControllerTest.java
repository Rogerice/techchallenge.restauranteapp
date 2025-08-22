package com.techchallenge.restauranteapp.infrastructure.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.restauranteapp.domain.application.service.UsuarioService;
import com.techchallenge.restauranteapp.domain.model.Usuario;

@WebMvcTest(controllers = UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @SuppressWarnings("removal")
	@MockBean
    private UsuarioService service;

    // Ajuste para setters se o domínio não tiver @Builder
    private Usuario novo(Long id, String nome, String email) {
        return Usuario.builder()
                .id(id)
                .nome(nome)
                .email(email)
                // acrescente .senha(...) / .tipoUsuario(...) se seu @Valid exigir
                .build();
    }

    @Test
    @DisplayName("POST /api/usuarios → 201, Location e ApiResponse com usuário criado")
    void criar() throws Exception {
        var entrada = novo(null, "Ana", "ana@ex.com");
        var criado  = novo(10L, "Ana", "ana@ex.com");

        when(service.criar(any(Usuario.class))).thenReturn(criado);

        mvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(entrada)))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", "/api/usuarios/10"))
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(201)))
           .andExpect(jsonPath("$.message", is("Usuário criado")))
           .andExpect(jsonPath("$.data.id", is(10)))
           .andExpect(jsonPath("$.data.nome", is("Ana")))
           .andExpect(jsonPath("$.data.email", is("ana@ex.com")));

        ArgumentCaptor<Usuario> cap = ArgumentCaptor.forClass(Usuario.class);
        verify(service).criar(cap.capture());
        assert "Ana".equals(cap.getValue().getNome());
        assert "ana@ex.com".equals(cap.getValue().getEmail());
    }

    @Test
    @DisplayName("GET /api/usuarios → 200 e lista")
    void listar() throws Exception {
        when(service.listar()).thenReturn(List.of(
                novo(1L, "Rafa", "r@ex.com"),
                novo(2L, "Bia", "b@ex.com")
        ));

        mvc.perform(get("/api/usuarios"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", is("OK")))
           .andExpect(jsonPath("$.data", hasSize(2)))
           .andExpect(jsonPath("$.data[0].id", is(1)))
           .andExpect(jsonPath("$.data[0].nome", is("Rafa")))
           .andExpect(jsonPath("$.data[1].id", is(2)))
           .andExpect(jsonPath("$.data[1].nome", is("Bia")));

        verify(service).listar();
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} → 200 e usuário")
    void buscar() throws Exception {
        when(service.buscar(5L)).thenReturn(novo(5L, "Carol", "c@ex.com"));

        mvc.perform(get("/api/usuarios/{id}", 5L))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", is("OK")))
           .andExpect(jsonPath("$.data.id", is(5)))
           .andExpect(jsonPath("$.data.nome", is("Carol")))
           .andExpect(jsonPath("$.data.email", is("c@ex.com")));

        verify(service).buscar(5L);
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} → 200 e atualizado")
    void atualizar() throws Exception {
        var entrada = novo(null, "Novo Nome", "novo@ex.com");
        var atualizado = novo(7L, "Novo Nome", "novo@ex.com");

        when(service.atualizar(eq(7L), any(Usuario.class))).thenReturn(atualizado);

        mvc.perform(put("/api/usuarios/{id}", 7L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(entrada)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", is("Atualizado")))
           .andExpect(jsonPath("$.data.id", is(7)))
           .andExpect(jsonPath("$.data.nome", is("Novo Nome")))
           .andExpect(jsonPath("$.data.email", is("novo@ex.com")));

        verify(service).atualizar(eq(7L), any(Usuario.class));
    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id} → 200 e retorna removido")
    void deletar() throws Exception {
        when(service.deletarERetornar(9L)).thenReturn(novo(9L, "Zé", "ze@ex.com"));

        mvc.perform(delete("/api/usuarios/{id}", 9L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", is("Usuário deletado")))
           .andExpect(jsonPath("$.data.id", is(9)))
           .andExpect(jsonPath("$.data.nome", is("Zé")));

        verify(service).deletarERetornar(9L);
    }
}
