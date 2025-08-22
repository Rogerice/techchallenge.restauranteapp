package com.techchallenge.restauranteapp.domain.model.entity;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.techchallenge.restauranteapp.domain.model.PojoTestUtils;

class ItemCardapioEntityTest {

  @Test
  void equalsHashCodeToString_basico() {
    RestauranteEntity r = RestauranteEntity.builder().id(10L).nome("Restô").build();

    ItemCardapioEntity a = ItemCardapioEntity.builder()
        .id(1L).nome("Coxinha").preco(new BigDecimal("10.00"))
        .somenteNoLocal(false).restaurante(r).build();

    ItemCardapioEntity sameId = ItemCardapioEntity.builder()
        .id(1L).nome("Coxinha").preco(new BigDecimal("10.00"))
        .somenteNoLocal(false).restaurante(r).build();

    ItemCardapioEntity differentId = ItemCardapioEntity.builder()
        .id(2L).nome("Coxinha").preco(new BigDecimal("10.00"))
        .somenteNoLocal(false).restaurante(r).build();

    PojoTestUtils.verifyEqualsHashCodeToString(a, sameId, differentId);
  }
}

