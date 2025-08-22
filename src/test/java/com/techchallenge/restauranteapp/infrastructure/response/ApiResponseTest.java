package com.techchallenge.restauranteapp.infrastructure.response;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiResponseTest {

    @Test
    @DisplayName("Construtor deve inicializar status, message, data e timestamp")
    void criarComValores() {
        String dado = "payload";
        ApiResponse<String> resp = new ApiResponse<>(200, "OK", dado);

        assertEquals(200, resp.getStatus());
        assertEquals("OK", resp.getMessage());
        assertEquals("payload", resp.getData());
        assertNotNull(resp.getTimestamp());

        // timestamp deve ser "agora" (pequena tolerância de segundos)
        LocalDateTime agora = LocalDateTime.now();
        assertTrue(!resp.getTimestamp().isAfter(agora));
    }

    @Test
    @DisplayName("Funciona com tipos genéricos diferentes")
    void tiposGenericos() {
        ApiResponse<Integer> respInt = new ApiResponse<>(201, "Criado", 123);
        assertEquals(201, respInt.getStatus());
        assertEquals(123, respInt.getData());

        record UserDTO(Long id, String nome) {}
        var user = new UserDTO(1L, "Ana");

        ApiResponse<UserDTO> respDto = new ApiResponse<>(200, "OK", user);
        assertEquals("Ana", respDto.getData().nome());
    }

    @Test
    @DisplayName("Message pode ser nula e data pode ser nula")
    void nulos() {
        ApiResponse<Object> resp = new ApiResponse<>(400, null, null);

        assertEquals(400, resp.getStatus());
        assertNull(resp.getMessage());
        assertNull(resp.getData());
        assertNotNull(resp.getTimestamp());
    }
}
