package com.techchallenge.restauranteapp.domain.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;

import com.techchallenge.restauranteapp.infrastructure.response.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

class ApiExceptionHandlerTest {

	private final ApiExceptionHandler handler = new ApiExceptionHandler();

	private MockHttpServletRequest req(String uri) {
		MockHttpServletRequest r = new MockHttpServletRequest();
		r.setRequestURI(uri);
		return r;
	}

	// ===== Helpers para MethodArgumentNotValidException =====

	static class DummyController {
		@SuppressWarnings("unused")
		void create(@jakarta.validation.Valid DummyDto dto) {
		}
	}

	static class DummyDto {
		@NotNull(message = "nome é obrigatório")
		public String nome;
	}

	private MethodParameter methodParameterForDto() {
		try {
			Method m = DummyController.class.getDeclaredMethod("create", DummyDto.class);
			return new MethodParameter(m, 0);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	@DisplayName("handleNotFound → 404 com corpo ApiError")
	void notFound() {
		HttpServletRequest req = req("/usuarios/1");
		ResponseEntity<ApiError> resp = handler
				.handleNotFound(new RecursoNaoEncontradoException("Usuário não encontrado"), req);

		assertEquals(404, resp.getStatusCode().value());
		assertEquals(404, resp.getBody().status());
		assertEquals("Recurso não encontrado", resp.getBody().error());
		assertEquals("Usuário não encontrado", resp.getBody().message());
		assertEquals("/usuarios/1", resp.getBody().path());
		assertNotNull(resp.getBody().timestamp());
	}

	@Test
	@DisplayName("handleValidacao → 400 domínio com corpo ApiError")
	void validacaoDominio() {
		HttpServletRequest req = req("/items");
		ResponseEntity<ApiError> resp = handler.handleValidacao(new ValidacaoException("preço inválido"), req);

		assertEquals(400, resp.getStatusCode().value());
		assertEquals("Dados inválidos", resp.getBody().error());
		assertEquals("preço inválido", resp.getBody().message());
		assertEquals("/items", resp.getBody().path());
	}

	@Test
	@DisplayName("handleRegra → 409 conflito")
	void violacaoDeRegra() {
		HttpServletRequest req = req("/items");
		ResponseEntity<ApiError> resp = handler.handleRegra(new ViolacaoDeRegraNegocioException("nome duplicado"), req);

		assertEquals(409, resp.getStatusCode().value());
		assertEquals("Conflito de dados", resp.getBody().error());
		assertEquals("nome duplicado", resp.getBody().message());
	}

	@Test
	@DisplayName("handleOperacaoNaoPermitida → 403")
	void operacaoNaoPermitida() {
		HttpServletRequest req = req("/admin");
		ResponseEntity<ApiError> resp = handler
				.handleOperacaoNaoPermitida(new OperacaoNaoPermitidaException("sem permissão"), req);

		assertEquals(403, resp.getStatusCode().value());
		assertEquals("Operação não permitida", resp.getBody().error());
		assertEquals("sem permissão", resp.getBody().message());
		assertEquals("/admin", resp.getBody().path());
	}

	@Test
	@DisplayName("handleMethodArgumentNotValid → 400 com lista de fields")
	void methodArgumentNotValid() throws Exception {
		// BindingResult com 2 erros de campo
		DummyDto dto = new DummyDto();
		BindingResult br = new BeanPropertyBindingResult(dto, "dummyDto");
		br.addError(new FieldError("dummyDto", "nome", null, false, null, null, "nome é obrigatório"));
		br.addError(new FieldError("dummyDto", "email", null, false, null, null, "email inválido"));

		MethodParameter mp = methodParameterForDto();

		MethodArgumentNotValidException manve = new MethodArgumentNotValidException(mp, br);

		HttpServletRequest req = req("/cadastros");
		ResponseEntity<ApiError> resp = handler.handleMethodArgumentNotValid(manve, req);

		assertEquals(400, resp.getStatusCode().value());
		ApiError body = resp.getBody();
		assertEquals("Dados inválidos", body.error());
		assertEquals("Um ou mais campos estão inválidos.", body.message());
	}

	@Test
	@DisplayName("handleConstraint → 400 com mensagem agregada")
	void constraintViolation() {
		// Gera violações reais via Validator
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		DummyDto dto = new DummyDto(); // nome = null → viola @NotNull
		Set<ConstraintViolation<DummyDto>> violations = validator.validate(dto);

		ConstraintViolationException cve = new ConstraintViolationException(violations);

		HttpServletRequest req = req("/pathvars");
		ResponseEntity<ApiError> resp = handler.handleConstraint(cve, req);

		assertEquals(400, resp.getStatusCode().value());
		assertEquals("Dados inválidos", resp.getBody().error());
		// A mensagem agrega "propertyPath: message"
		assertTrue(resp.getBody().message().contains("nome"));
		assertTrue(resp.getBody().message().contains("obrigatório"));
	}

	@Test
	@DisplayName("handleDataIntegrity → 409 Violação de integridade")
	void dataIntegrity() {
		HttpServletRequest req = req("/items");
		ResponseEntity<ApiError> resp = handler
				.handleDataIntegrity(new DataIntegrityViolationException("duplicate key"), req);

		assertEquals(409, resp.getStatusCode().value());
		assertEquals("Violação de integridade", resp.getBody().error());
		assertEquals("Operação violaria restrições do banco.", resp.getBody().message());
	}

	@Test
	@DisplayName("handleUncaught → 500 Erro interno")
	void uncaught() {
		HttpServletRequest req = req("/boom");
		ResponseEntity<ApiError> resp = handler.handleUncaught(new RuntimeException("ops"), req);

		assertEquals(500, resp.getStatusCode().value());
		assertEquals("Erro interno do servidor", resp.getBody().error());
		assertEquals("Erro inesperado.", resp.getBody().message());
		// timestamp razoável (não nulo e próximo de agora)
		assertNotNull(resp.getBody().timestamp());
		assertTrue(resp.getBody().timestamp().isBefore(OffsetDateTime.now().plusSeconds(5)));
	}
}
