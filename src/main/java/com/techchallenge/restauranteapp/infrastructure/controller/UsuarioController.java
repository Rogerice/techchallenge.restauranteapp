package com.techchallenge.restauranteapp.infrastructure.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techchallenge.restauranteapp.domain.application.service.UsuarioService;
import com.techchallenge.restauranteapp.domain.model.Usuario;
import com.techchallenge.restauranteapp.infrastructure.response.ApiResponse;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	private final UsuarioService service;

	public UsuarioController(UsuarioService s) {
		this.service = s;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Usuario>> criar(@Valid @RequestBody Usuario u) {
		Usuario criado = service.criar(u);
		return ResponseEntity.created(URI.create("/api/usuarios/" + criado.getId()))
				.body(new ApiResponse<>(201, "Usuário criado", criado));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<Usuario>>> listar() {
		var lista = service.listar();
		return ResponseEntity.ok(new ApiResponse<>(200, "OK", lista));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Usuario>> buscar(@PathVariable Long id) {
		var u = service.buscar(id);
		return ResponseEntity.ok(new ApiResponse<>(200, "OK", u));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Usuario>> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario u) {
		var up = service.atualizar(id, u);
		return ResponseEntity.ok(new ApiResponse<>(200, "Atualizado", up));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Usuario>> deletar(@PathVariable Long id) {
		Usuario removido = service.deletarERetornar(id);
		return ResponseEntity.ok(new ApiResponse<>(200, "Usuário deletado", removido));
	}
}