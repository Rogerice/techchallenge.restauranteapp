package com.techchallenge.restauranteapp.domain.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.techchallenge.restauranteapp.domain.enums.TipoUsuarioEnum;
import com.techchallenge.restauranteapp.domain.model.PojoTestUtils;

class UsuarioEntityTest {

  @Test
  void equalsHashCodeToString_ok() {
    UsuarioEntity a = UsuarioEntity.builder()
        .id(1L)
        .nome("Ana")
        .email("ana@ex.com")
        .senha("s3nh@")
        .tipoUsuario(TipoUsuarioEnum.CLIENTE)
        .build();

    UsuarioEntity same = UsuarioEntity.builder()
        .id(1L)
        .nome("Ana")
        .email("ana@ex.com")
        .senha("s3nh@")
        .tipoUsuario(TipoUsuarioEnum.CLIENTE)
        .build();

    UsuarioEntity different = UsuarioEntity.builder()
        .id(2L)
        .nome("Bruno")
        .email("bruno@ex.com")
        .senha("outra")
        .tipoUsuario(TipoUsuarioEnum.ADMIN)
        .build();

    PojoTestUtils.verifyEqualsHashCodeToString(a, same, different);
  }

  @Test
  void builderGettersSetters_basico() {
    UsuarioEntity u = UsuarioEntity.builder()
        .id(10L)
        .nome("Carla")
        .email("carla@ex.com")
        .senha("123")
        .tipoUsuario(TipoUsuarioEnum.DONO)
        .build();

    assertEquals(10L, u.getId());
    assertEquals("Carla", u.getNome());
    assertEquals("carla@ex.com", u.getEmail());
    assertEquals("123", u.getSenha());
    assertEquals(TipoUsuarioEnum.DONO, u.getTipoUsuario());

    // Exercita setters também
    u.setNome("Carla Souza");
    u.setSenha("456");
    assertEquals("Carla Souza", u.getNome());
    assertEquals("456", u.getSenha());
  }
}
