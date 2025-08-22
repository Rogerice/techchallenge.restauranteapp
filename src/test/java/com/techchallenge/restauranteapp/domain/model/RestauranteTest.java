package com.techchallenge.restauranteapp.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RestauranteTest {

  @Test
  void equalsHashCodeToString_ok() {
    Restaurante a = Restaurante.builder()
        .id(1L)
        .nome("Cantina da Esquina")
        .cnpj("11111111000111")
        .endereco("Rua A, 123")
        .tipoCozinha("Italiana")
        .horarioFuncionamento("11:00-23:00")
        .donoUsuarioId(10L)
        .build();

    Restaurante same = Restaurante.builder()
        .id(1L)
        .nome("Cantina da Esquina")
        .cnpj("11111111000111")
        .endereco("Rua A, 123")
        .tipoCozinha("Italiana")
        .horarioFuncionamento("11:00-23:00")
        .donoUsuarioId(10L)
        .build();

    Restaurante different = Restaurante.builder()
        .id(2L)
        .nome("Bistrô Central")
        .cnpj("22222222000122")
        .endereco("Av. B, 456")
        .tipoCozinha("Francesa")
        .horarioFuncionamento("12:00-22:00")
        .donoUsuarioId(20L)
        .build();

    PojoTestUtils.verifyEqualsHashCodeToString(a, same, different);
  }

  @Test
  void builderGettersSetters_basico() {
    Restaurante r = Restaurante.builder()
        .id(7L)
        .nome("La Parrilla")
        .cnpj("33333333000133")
        .endereco("Rua C, 789")
        .tipoCozinha("Parrilla")
        .horarioFuncionamento("10:00-22:00")
        .donoUsuarioId(99L)
        .build();

    assertEquals(7L, r.getId());
    assertEquals("La Parrilla", r.getNome());
    assertEquals("33333333000133", r.getCnpj());
    assertEquals("Rua C, 789", r.getEndereco());
    assertEquals("Parrilla", r.getTipoCozinha());
    assertEquals("10:00-22:00", r.getHorarioFuncionamento());
    assertEquals(99L, r.getDonoUsuarioId());

    // Exercita setters também
    r.setNome("La Parrilla Prime");
    r.setEndereco("Alameda Nova, 1000");
    r.setDonoUsuarioId(100L);

    assertEquals("La Parrilla Prime", r.getNome());
    assertEquals("Alameda Nova, 1000", r.getEndereco());
    assertEquals(100L, r.getDonoUsuarioId());
  }
}
