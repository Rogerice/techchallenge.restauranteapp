package com.techchallenge.restauranteapp.domain.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TipoUsuarioEnumTest {

    @Test
    @DisplayName("Deve conter os 3 tipos de usuário definidos")
    void deveConterTodosOsTipos() {
        assertEquals(3, TipoUsuarioEnum.values().length);

        assertTrue(contains(TipoUsuarioEnum.CLIENTE));
        assertTrue(contains(TipoUsuarioEnum.DONO));
        assertTrue(contains(TipoUsuarioEnum.ADMIN));
    }

    @Test
    @DisplayName("valueOf deve converter string corretamente para enum")
    void valueOf_ok() {
        assertEquals(TipoUsuarioEnum.CLIENTE, TipoUsuarioEnum.valueOf("CLIENTE"));
        assertEquals(TipoUsuarioEnum.DONO, TipoUsuarioEnum.valueOf("DONO"));
        assertEquals(TipoUsuarioEnum.ADMIN, TipoUsuarioEnum.valueOf("ADMIN"));
    }

    @Test
    @DisplayName("valueOf deve lançar IllegalArgumentException para string inválida")
    void valueOf_invalido() {
        assertThrows(IllegalArgumentException.class, () -> TipoUsuarioEnum.valueOf("GERENTE"));
    }

    private boolean contains(TipoUsuarioEnum e) {
        for (TipoUsuarioEnum v : TipoUsuarioEnum.values()) {
            if (v == e) return true;
        }
        return false;
    }
}
