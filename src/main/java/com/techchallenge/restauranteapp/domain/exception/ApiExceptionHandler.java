package com.techchallenge.restauranteapp.domain.exception;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.techchallenge.restauranteapp.infrastructure.response.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	public ResponseEntity<ApiError> handleNotFound(RecursoNaoEncontradoException ex, HttpServletRequest req) {
		return build(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage(), req);
	}

	@ExceptionHandler(ValidacaoException.class)
	public ResponseEntity<ApiError> handleValidacao(ValidacaoException ex, HttpServletRequest req) {
		return build(HttpStatus.BAD_REQUEST, "Dados inválidos", ex.getMessage(), req);
	}

	@ExceptionHandler(ViolacaoDeRegraNegocioException.class)
	public ResponseEntity<ApiError> handleRegra(ViolacaoDeRegraNegocioException ex, HttpServletRequest req) {
		return build(HttpStatus.CONFLICT, "Conflito de dados", ex.getMessage(), req);
	}

	@ExceptionHandler(OperacaoNaoPermitidaException.class)
	public ResponseEntity<ApiError> handleOperacaoNaoPermitida(OperacaoNaoPermitidaException ex,
			HttpServletRequest req) {
		return build(HttpStatus.FORBIDDEN, "Operação não permitida", ex.getMessage(), req);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpServletRequest req) {
		var fields = ex.getBindingResult().getFieldErrors().stream()
				.map(fe -> new ApiError.FieldError(fe.getField(), fe.getDefaultMessage())).collect(Collectors.toList());

		var body = new ApiError(HttpStatus.BAD_REQUEST.value(), "Dados inválidos", "Um ou mais campos estão inválidos.",
				req.getRequestURI(), OffsetDateTime.now(), fields);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiError> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
		String msg = ex.getConstraintViolations().stream().map(v -> v.getPropertyPath() + ": " + v.getMessage())
				.collect(Collectors.joining("; "));
		return build(HttpStatus.BAD_REQUEST, "Dados inválidos", msg, req);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
		return build(HttpStatus.CONFLICT, "Violação de integridade", "Operação violaria restrições do banco.", req);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleUncaught(Exception ex, HttpServletRequest req) {
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", "Erro inesperado.", req);
	}

	private ResponseEntity<ApiError> build(HttpStatus status, String error, String message, HttpServletRequest req) {
		var body = new ApiError(status.value(), error, message, req.getRequestURI(), OffsetDateTime.now(), null);
		return ResponseEntity.status(status).body(body);
	}
}
