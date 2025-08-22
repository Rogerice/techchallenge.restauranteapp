package com.techchallenge.restauranteapp.infrastructure.response;

import static org.junit.jupiter.api.Assertions.*;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiErrorTest {

    @Test
    @DisplayName("Deve criar ApiError com todos os campos preenchidos")
    void criarCompleto() {
        OffsetDateTime now = OffsetDateTime.now();
        var fieldErrors = List.of(
                new ApiError.FieldError("nome", "não pode ser vazio"),
                new ApiError.FieldError("email", "formato inválido")
        );

        ApiError err = new ApiError(
                400,
                "Dados inválidos",
                "Erro de validação",
                "/api/usuarios",
                now,
                fieldErrors
        );

        assertEquals(400, err.status());
        assertEquals("Dados inválidos", err.error());
        assertEquals("Erro de validação", err.message());
        assertEquals("/api/usuarios", err.path());
        assertEquals(now, err.timestamp());
        assertEquals(2, err.fieldErrors().size());
        assertEquals("nome", err.fieldErrors().get(0).field());
        assertEquals("não pode ser vazio", err.fieldErrors().get(0).message());
    }

    @Test
    @DisplayName("Deve criar ApiError sem fieldErrors")
    void criarSemFieldErrors() {
        OffsetDateTime now = OffsetDateTime.now();

        ApiError err = new ApiError(
                404,
                "Recurso não encontrado",
                "Usuário não existe",
                "/api/usuarios/99",
                now,
                null
        );

        assertEquals(404, err.status());
        assertEquals("Recurso não encontrado", err.error());
        assertEquals("Usuário não existe", err.message());
        assertEquals("/api/usuarios/99", err.path());
        assertEquals(now, err.timestamp());
        assertNull(err.fieldErrors());
    }

    @Test
    @DisplayName("FieldError deve armazenar corretamente campo e mensagem")
    void fieldError() {
        var fe = new ApiError.FieldError("senha", "mínimo 6 caracteres");

        assertEquals("senha", fe.field());
        assertEquals("mínimo 6 caracteres", fe.message());
    }
}
