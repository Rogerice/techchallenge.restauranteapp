package com.techchallenge.restauranteapp.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techchallenge.restauranteapp.domain.application.service.ItemCardapioService;
import com.techchallenge.restauranteapp.domain.model.ItemCardapio;
import com.techchallenge.restauranteapp.infrastructure.response.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cardapio/itens")
public class ItemCardapioController {

    private final ItemCardapioService service;

    public ItemCardapioController(ItemCardapioService service) {
        this.service = service;
    }

    @GetMapping("/_ping")
    public String ping() { return "ok"; }

    @PostMapping("/restaurantes/{restauranteId}")
    public ResponseEntity<ApiResponse<ItemCardapio>> criar(
            @PathVariable Long restauranteId,
            @Valid @RequestBody ItemCardapio item) {

        ItemCardapio criado = service.criar(restauranteId, item);
        return ResponseEntity.status(201)
                .body(new ApiResponse<>(201, "Item criado", criado));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemCardapio>>> listarTodos() {
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", service.listarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemCardapio>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", service.buscarPorId(id)));
    }

    @GetMapping("/restaurantes/{restauranteId}")
    public ResponseEntity<ApiResponse<List<ItemCardapio>>> listarPorRestaurante(@PathVariable Long restauranteId) {
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", service.listarPorRestaurante(restauranteId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemCardapio>> atualizar(
            @PathVariable Long id, @Valid @RequestBody ItemCardapio item) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Atualizado", service.atualizar(id, item)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemCardapio>> deletar(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Removido", service.deletarERetornar(id)));
    }
}
