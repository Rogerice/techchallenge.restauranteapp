package com.techchallenge.restauranteapp.domain.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.techchallenge.restauranteapp.domain.model.PojoTestUtils;

class RestauranteEntityTest {

  @Test
  void equalsHashCodeToString_ok() {
    // Mantemos 'dono' como null para não envolver proxy/associação no equals do Lombok.
    RestauranteEntity a = RestauranteEntity.builder()
        .id(1L)
        .nome("La Nonna")
        .cnpj("12345678000190")
        .endereco("Rua A, 100")
        .tipoCozinha("Italiana")
        .horarioFuncionamento("10:00-22:00")
        .dono(null)
        .build();

    RestauranteEntity same = RestauranteEntity.builder()
        .id(1L)
        .nome("La Nonna")
        .cnpj("12345678000190")
        .endereco("Rua A, 100")
        .tipoCozinha("Italiana")
        .horarioFuncionamento("10:00-22:00")
        .dono(null)
        .build();

    RestauranteEntity different = RestauranteEntity.builder()
        .id(2L)
        .nome("Bistrô do Centro")
        .cnpj("98765432000100")
        .endereco("Rua B, 200")
        .tipoCozinha("Francesa")
        .horarioFuncionamento("11:00-23:00")
        .dono(null)
        .build();

    // Usa a util para cobrir equals, hashCode e toString
    PojoTestUtils.verifyEqualsHashCodeToString(a, same, different);
  }

  @Test
  void builderESettersGetters_basico() {
    RestauranteEntity r = RestauranteEntity.builder()
        .id(10L)
        .nome("Cantina da Praça")
        .cnpj("11122233000144")
        .endereco("Av. Principal, 500")
        .tipoCozinha("Brasileira")
        .horarioFuncionamento("09:00-21:00")
        .dono(null)
        .build();

    // Pequena checagem manual dos accessors (rápida e estável)
    assertEquals(10L, r.getId());
    assertEquals("Cantina da Praça", r.getNome());
    assertEquals("11122233000144", r.getCnpj());
    assertEquals("Av. Principal, 500", r.getEndereco());
    assertEquals("Brasileira", r.getTipoCozinha());
    assertEquals("09:00-21:00", r.getHorarioFuncionamento());

    // também garante que os setters existem e funcionam
    r.setNome("Cantina Nova");
    assertEquals("Cantina Nova", r.getNome());
  }
}
