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
import com.techchallenge.restauranteapp.domain.application.service.RestauranteService;
import com.techchallenge.restauranteapp.domain.model.Restaurante;

@WebMvcTest(controllers = RestauranteController.class)
class RestauranteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private RestauranteService service;

    private Restaurante novoRestaurante(Long id, String nome) {
        // Ajuste para setters se não usar @Builder
        return Restaurante.builder()
                .id(id)
                .nome(nome)
                .cnpj("12345678000190")
                .endereco("Rua X, 123")
                .horarioFuncionamento("08:00-18:00")
                .tipoCozinha("BRASILEIRA")
                .build();
    }

    @Test
    @DisplayName("POST /api/restaurantes → 201, Location e ApiResponse com criado")
    void criar() throws Exception {
        Restaurante entrada = novoRestaurante(null, "Casa da Esfiha");
        Restaurante criado = novoRestaurante(10L, "Casa da Esfiha");

        when(service.criar(any(Restaurante.class))).thenReturn(criado);

        mvc.perform(post("/api/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(entrada)))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", "/api/restaurantes/10"))
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(201)))
           .andExpect(jsonPath("$.message", containsString("Restaurante criado")))
           .andExpect(jsonPath("$.data.id", is(10)))
           .andExpect(jsonPath("$.data.nome", is("Casa da Esfiha")));

        ArgumentCaptor<Restaurante> cap = ArgumentCaptor.forClass(Restaurante.class);
        verify(service).criar(cap.capture());
        assert "Casa da Esfiha".equals(cap.getValue().getNome());
    }

    @Test
    @DisplayName("GET /api/restaurantes → 200 com lista preenchida")
    void listarTodos_ok() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(
                novoRestaurante(1L, "R1"),
                novoRestaurante(2L, "R2")
        ));

        mvc.perform(get("/api/restaurantes"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", containsString("listados com sucesso")))
           .andExpect(jsonPath("$.data", hasSize(2)))
           .andExpect(jsonPath("$.data[0].nome", is("R1")))
           .andExpect(jsonPath("$.data[1].nome", is("R2")));

        verify(service).listarTodos();
    }

    @Test
    @DisplayName("GET /api/restaurantes → 200 com lista vazia e mensagem adequada")
    void listarTodos_vazio() throws Exception {
        when(service.listarTodos()).thenReturn(List.of());

        mvc.perform(get("/api/restaurantes"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", is("Nenhum restaurante encontrado")))
           .andExpect(jsonPath("$.data", hasSize(0)));

        verify(service).listarTodos();
    }

    @Test
    @DisplayName("GET /api/restaurantes/{id} → 200 e restaurante")
    void buscarPorId() throws Exception {
        when(service.buscarPorId(5L)).thenReturn(novoRestaurante(5L, "R5"));

        mvc.perform(get("/api/restaurantes/{id}", 5L))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", is("Restaurante encontrado")))
           .andExpect(jsonPath("$.data.id", is(5)))
           .andExpect(jsonPath("$.data.nome", is("R5")));

        verify(service).buscarPorId(5L);
    }

    @Test
    @DisplayName("PUT /api/restaurantes/{id} → 200 e atualizado")
    void atualizar() throws Exception {
        Restaurante entrada = novoRestaurante(null, "Novo Nome");
        Restaurante atualizado = novoRestaurante(7L, "Novo Nome");

        when(service.atualizar(eq(7L), any(Restaurante.class))).thenReturn(atualizado);

        mvc.perform(put("/api/restaurantes/{id}", 7L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(entrada)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", is("Restaurante atualizado com sucesso")))
           .andExpect(jsonPath("$.data.id", is(7)))
           .andExpect(jsonPath("$.data.nome", is("Novo Nome")));

        verify(service).atualizar(eq(7L), any(Restaurante.class));
    }

    @Test
    @DisplayName("DELETE /api/restaurantes/{id} → 200 e ApiResponse com timestamp")
    void deletar() throws Exception {
        doNothing().when(service).deletar(9L);

        mvc.perform(delete("/api/restaurantes/{id}", 9L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", is("Restaurante deletado com sucesso")))
           // é um LocalDateTime serializado; só checo que existe e é string
           .andExpect(jsonPath("$.data", notNullValue()));
           //.andExpect(jsonPath("$.data", isA(String.class)));

        verify(service).deletar(9L);
    }
}
