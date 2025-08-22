package com.techchallenge.restauranteapp.infrastructure.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
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
import com.techchallenge.restauranteapp.domain.application.service.ItemCardapioService;
import com.techchallenge.restauranteapp.domain.model.ItemCardapio;
import com.techchallenge.restauranteapp.domain.model.Restaurante;

@WebMvcTest(controllers = ItemCardapioController.class)
class ItemCardapioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private ItemCardapioService service;

    private ItemCardapio novoItem(Long id, String nome, String preco) {
        return ItemCardapio.builder()
                .id(id)
                .nome(nome)
                .descricao("desc")
                .preco(new BigDecimal(preco))
                .disponivel(true)
                .somenteNoLocal(false)
                .caminhoFoto("/img.png")
                .restaurante(Restaurante.builder().id(99L).build())
                .build();
    }

    @Test
    @DisplayName("GET /api/cardapio/itens/_ping → 200 ok (texto simples)")
    void ping() throws Exception {
        mvc.perform(get("/api/cardapio/itens/_ping"))
           .andExpect(status().isOk())
           .andExpect(content().string("ok"));
        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("POST /api/cardapio/itens/restaurantes/{id} → 201 e ApiResponse com item criado")
    void criar() throws Exception {
        Long restId = 77L;
        ItemCardapio entrada = novoItem(null, "Burger", "25.00");
        ItemCardapio criado  = novoItem(10L, "Burger", "25.00");

        when(service.criar(eq(restId), any(ItemCardapio.class))).thenReturn(criado);

        mvc.perform(post("/api/cardapio/itens/restaurantes/{restauranteId}", restId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(entrada)))
           .andExpect(status().isCreated())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(201)))
           .andExpect(jsonPath("$.message", containsString("Item criado")))
           .andExpect(jsonPath("$.data.id", is(10)))
           .andExpect(jsonPath("$.data.nome", is("Burger")))
           .andExpect(jsonPath("$.data.preco", comparesEqualTo(25.00)));

        ArgumentCaptor<ItemCardapio> cap = ArgumentCaptor.forClass(ItemCardapio.class);
        verify(service).criar(eq(restId), cap.capture());
        // checa que payload chegou ao service
        ItemCardapio enviado = cap.getValue();
        // nome e preco do corpo
        assert "Burger".equals(enviado.getNome());
        assert new BigDecimal("25.00").compareTo(enviado.getPreco()) == 0;
    }

    @Test
    @DisplayName("GET /api/cardapio/itens → 200 e lista")
    void listarTodos() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(
                novoItem(1L, "A", "10.00"),
                novoItem(2L, "B", "20.00")
        ));

        mvc.perform(get("/api/cardapio/itens"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.data", hasSize(2)))
           .andExpect(jsonPath("$.data[0].nome", is("A")))
           .andExpect(jsonPath("$.data[1].nome", is("B")));

        verify(service).listarTodos();
    }

    @Test
    @DisplayName("GET /api/cardapio/itens/{id} → 200 e item")
    void buscarPorId() throws Exception {
        when(service.buscarPorId(5L)).thenReturn(novoItem(5L, "Soda", "7.50"));

        mvc.perform(get("/api/cardapio/itens/{id}", 5L))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.data.id", is(5)))
           .andExpect(jsonPath("$.data.nome", is("Soda")))
           .andExpect(jsonPath("$.data.preco", comparesEqualTo(7.50)));

        verify(service).buscarPorId(5L);
    }

    @Test
    @DisplayName("GET /api/cardapio/itens/restaurantes/{id} → 200 e lista do restaurante")
    void listarPorRestaurante() throws Exception {
        when(service.listarPorRestaurante(99L)).thenReturn(List.of(
                novoItem(3L, "C", "12.00")
        ));

        mvc.perform(get("/api/cardapio/itens/restaurantes/{restauranteId}", 99L))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.data", hasSize(1)))
           .andExpect(jsonPath("$.data[0].id", is(3)));

        verify(service).listarPorRestaurante(99L);
    }

    @Test
    @DisplayName("PUT /api/cardapio/itens/{id} → 200 e item atualizado")
    void atualizar() throws Exception {
        ItemCardapio entrada = novoItem(null, "Burger X", "30.00");
        ItemCardapio atualizado = novoItem(10L, "Burger X", "30.00");

        when(service.atualizar(eq(10L), any(ItemCardapio.class))).thenReturn(atualizado);

        mvc.perform(put("/api/cardapio/itens/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(entrada)))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", containsString("Atualizado")))
           .andExpect(jsonPath("$.data.id", is(10)))
           .andExpect(jsonPath("$.data.nome", is("Burger X")));

        verify(service).atualizar(eq(10L), any(ItemCardapio.class));
    }

    @Test
    @DisplayName("DELETE /api/cardapio/itens/{id} → 200 e retorna item removido")
    void deletar() throws Exception {
        when(service.deletarERetornar(7L)).thenReturn(novoItem(7L, "Del", "15.00"));

        mvc.perform(delete("/api/cardapio/itens/{id}", 7L))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.status", is(200)))
           .andExpect(jsonPath("$.message", containsString("Removido")))
           .andExpect(jsonPath("$.data.id", is(7)))
           .andExpect(jsonPath("$.data.nome", is("Del")));

        verify(service).deletarERetornar(7L);
    }
}
