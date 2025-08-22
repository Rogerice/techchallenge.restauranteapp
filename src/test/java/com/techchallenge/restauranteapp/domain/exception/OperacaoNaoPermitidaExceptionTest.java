package com.techchallenge.restauranteapp.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperacaoNaoPermitidaExceptionTest {

    @Test
    void deveLancarExcecaoComMensagemCorreta() {
        String mensagem = "Operação não é permitida neste contexto";

        OperacaoNaoPermitidaException excecao = assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> { throw new OperacaoNaoPermitidaException(mensagem); }
        );

        assertEquals(mensagem, excecao.getMessage());
    }
}
