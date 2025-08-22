package com.techchallenge.restauranteapp.infrastructure.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.techchallenge.restauranteapp.domain.application.service.TipoUsuarioService;
import com.techchallenge.restauranteapp.domain.model.TipoUsuario;

@WebMvcTest(controllers = TipoUsuarioController.class)
class TipoUsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private TipoUsuarioService service;

    // Ajuste para setters se sua classe não tiver @Builder
    private TipoUsuario novo(Long id, String nomeTipo) {
        return TipoUsuario.builder()
                .id(id)
                .nomeTipo(nomeTipo)
                .build();
    }

    @Test
    @DisplayName("POST /api/tipos-usuario → 201 com Location e ApiResponse(data)")
    void criar() throws Exception {
        var entrada = novo(null, "GERENTE");
        var criado  = novo(10L, "GERENTE");

        when(service.criar(any(TipoUsuario.class))).thenReturn(criado);

        mvc.perform(post("/api/tipos-usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(entrada)))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", "/api/tipos-usuario/10"))
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(201)))
           .andExpect(jsonPath("$.message", is("Tipo criado")))
           .andExpect(jsonPath("$.data.id", is(10)))
           .andExpect(jsonPath("$.data.nomeTipo", is("GERENTE")));

        ArgumentCaptor<TipoUsuario> cap = ArgumentCaptor.forClass(TipoUsuario.class);
        verify(service).criar(cap.capture());
        assert "GERENTE".equals(cap.getValue().getNomeTipo());
    }

    @Test
    @DisplayName("GET /api/tipos-usuario → 200 e lista")
    void listar() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(
                novo(1L, "CLIENTE"),
                novo(2L, "DONO")
        ));

        mvc.perform(get("/api/tipos-usuario"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", is("Lista de Usuarios")))
           .andExpect(jsonPath("$.data", hasSize(2)))
           .andExpect(jsonPath("$.data[0].nomeTipo", is("CLIENTE")))
           .andExpect(jsonPath("$.data[1].nomeTipo", is("DONO")));

        verify(service).listarTodos();
    }

    @Test
    @DisplayName("GET /api/tipos-usuario/{id} → 200 e item")
    void buscar() throws Exception {
        when(service.buscarPorIdOuFalhar(5L)).thenReturn(novo(5L, "ADMIN"));

        mvc.perform(get("/api/tipos-usuario/{id}", 5L))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", is("Busca executada")))
           .andExpect(jsonPath("$.data.id", is(5)))
           .andExpect(jsonPath("$.data.nomeTipo", is("ADMIN")));

        verify(service).buscarPorIdOuFalhar(5L);
    }

    @Test
    @DisplayName("PUT /api/tipos-usuario/{id} → 200 e atualizado")
    void atualizar() throws Exception {
        var entrada = novo(null, "CAIXA");
        var atualizado = novo(7L, "CAIXA");

        when(service.atualizar(eq(7L), any(TipoUsuario.class))).thenReturn(atualizado);

        mvc.perform(put("/api/tipos-usuario/{id}", 7L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(entrada)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", is("Atualizado")))
           .andExpect(jsonPath("$.data.id", is(7)))
           .andExpect(jsonPath("$.data.nomeTipo", is("CAIXA")));

        verify(service).atualizar(eq(7L), any(TipoUsuario.class));
    }

    @Test
    @DisplayName("DELETE /api/tipos-usuario/{id} → 200 e retorna removido")
    void deletar() throws Exception {
        when(service.deletar(9L)).thenReturn(novo(9L, "TEMP"));

        mvc.perform(delete("/api/tipos-usuario/{id}", 9L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", is("Tipo de usuário deletado com sucesso")))
           .andExpect(jsonPath("$.data.id", is(9)))
           .andExpect(jsonPath("$.data.nomeTipo", is("TEMP")));

        verify(service).deletar(9L);
    }
}
