package com.techchallenge.restauranteapp.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidacaoExceptionTest {

    @Test
    void deveCriarValidacaoExceptionComMensagem() {
        // Arrange
        String mensagemEsperada = "Erro de validação";

        // Act
        ValidacaoException exception = new ValidacaoException(mensagemEsperada);

        // Assert
        assertEquals(mensagemEsperada, exception.getMessage());
    }
}
