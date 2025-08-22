package com.techchallenge.restauranteapp.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecursoNaoEncontradoExceptionTest {

    @Test
    void deveLancarExcecaoComMensagemCorreta() {
        String mensagem = "Recurso com ID 123 não encontrado";

        RecursoNaoEncontradoException excecao = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> { throw new RecursoNaoEncontradoException(mensagem); }
        );

        assertEquals(mensagem, excecao.getMessage());
    }
}
