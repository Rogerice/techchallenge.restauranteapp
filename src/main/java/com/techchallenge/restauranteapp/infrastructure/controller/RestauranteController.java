package com.techchallenge.restauranteapp.infrastructure.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techchallenge.restauranteapp.domain.application.service.RestauranteService;
import com.techchallenge.restauranteapp.domain.model.Restaurante;
import com.techchallenge.restauranteapp.infrastructure.response.ApiResponse;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {

    private final RestauranteService service;

    public RestauranteController(RestauranteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Restaurante>> criar(@RequestBody Restaurante restaurante) {
        Restaurante criado = service.criar(restaurante);
        return ResponseEntity
            .created(URI.create("/api/restaurantes/" + criado.getId()))
            .body(new ApiResponse<>(201, "Restaurante criado com sucesso", criado));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Restaurante>>> listarTodos() {
        List<Restaurante> lista = service.listarTodos();
        String msg = lista.isEmpty() ? "Nenhum restaurante encontrado" : "Restaurantes listados com sucesso";
        return ResponseEntity.ok(new ApiResponse<>(200, msg, lista));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Restaurante>> buscarPorId(@PathVariable Long id) {
        Restaurante restaurante = service.buscarPorId(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Restaurante encontrado", restaurante));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Restaurante>> atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {
        Restaurante atualizado = service.atualizar(id, restaurante);
        return ResponseEntity.ok(new ApiResponse<>(200, "Restaurante atualizado com sucesso", atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<LocalDateTime>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Restaurante deletado com sucesso", LocalDateTime.now()));
    }
}
