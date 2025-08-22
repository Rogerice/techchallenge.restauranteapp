package com.techchallenge.restauranteapp.infrastructure.controller;

import com.techchallenge.restauranteapp.domain.application.service.TipoUsuarioService;
import com.techchallenge.restauranteapp.domain.model.TipoUsuario;
import com.techchallenge.restauranteapp.infrastructure.response.ApiResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-usuario")
public class TipoUsuarioController {
	private final TipoUsuarioService service;

	public TipoUsuarioController(TipoUsuarioService s) {
		this.service = s;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<TipoUsuario>> criar(@RequestBody TipoUsuario t) {
		var criado = service.criar(t);
		return ResponseEntity.created(URI.create("/api/tipos-usuario/" + criado.getId()))
				.body(new ApiResponse<>(201, "Tipo criado", criado));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<TipoUsuario>>> listar() {
		var lista = service.listarTodos();
		return ResponseEntity.ok(new ApiResponse<>(200, "Lista de Usuarios", lista));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<TipoUsuario>> buscar(@PathVariable Long id) {
		var t = service.buscarPorIdOuFalhar(id);
		return ResponseEntity.ok(new ApiResponse<>(200, "Busca executada", t));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<TipoUsuario>> atualizar(@PathVariable Long id, @RequestBody TipoUsuario t) {
		var up = service.atualizar(id, t);
		return ResponseEntity.ok(new ApiResponse<>(200, "Atualizado", up));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<TipoUsuario>> deletar(@PathVariable Long id) {
		TipoUsuario removido = service.deletar(id);
		return ResponseEntity.ok(new ApiResponse<>(200, "Tipo de usuário deletado com sucesso", removido));
	}

}
