package com.techchallenge.restauranteapp.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class ItemCardapioTest {

  private static final Validator VALIDATOR =
      Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void equalsHashCodeToString_ok() {
    ItemCardapio a = ItemCardapio.builder()
        .id(1L)
        .nome("Hambúrguer")
        .descricao("Pão, carne e queijo")
        .preco(new BigDecimal("29.90"))
        .disponivel(true)
        .caminhoFoto("/imgs/burger.png")
        .restaurante(null) // não precisamos instanciar Restaurante aqui
        .somenteNoLocal(false)
        .build();

    ItemCardapio same = ItemCardapio.builder()
        .id(1L)
        .nome("Hambúrguer")
        .descricao("Pão, carne e queijo")
        .preco(new BigDecimal("29.90"))
        .disponivel(true)
        .caminhoFoto("/imgs/burger.png")
        .restaurante(null)
        .somenteNoLocal(false)
        .build();

    ItemCardapio different = ItemCardapio.builder()
        .id(2L)
        .nome("Batata Frita")
        .descricao("Porção média")
        .preco(new BigDecimal("14.90"))
        .disponivel(true)
        .caminhoFoto("/imgs/fries.png")
        .restaurante(null)
        .somenteNoLocal(true)
        .build();

    PojoTestUtils.verifyEqualsHashCodeToString(a, same, different);
  }

  @Test
  void builderGettersSetters_basico() {
    ItemCardapio item = ItemCardapio.builder()
        .id(10L)
        .nome("Suco")
        .descricao("Laranja natural")
        .preco(new BigDecimal("8.50"))
        .disponivel(false)
        .caminhoFoto("/imgs/suco.png")
        .restaurante(null)
        .somenteNoLocal(false)
        .build();

    assertEquals(10L, item.getId());
    assertEquals("Suco", item.getNome());
    assertEquals("Laranja natural", item.getDescricao());
    assertEquals(new BigDecimal("8.50"), item.getPreco());
    assertFalse(item.getDisponivel());
    assertEquals("/imgs/suco.png", item.getCaminhoFoto());
    assertNull(item.getRestaurante());
    assertFalse(item.getSomenteNoLocal());

    // Exercita setters também
    item.setNome("Suco de Uva");
    item.setPreco(new BigDecimal("9.00"));
    item.setDisponivel(true);
    assertEquals("Suco de Uva", item.getNome());
    assertEquals(new BigDecimal("9.00"), item.getPreco());
    assertTrue(item.getDisponivel());
  }

  @Test
  void beanValidation_violaçõesDeCampos() {
    // nome em branco -> @NotBlank
    ItemCardapio invalidoNome = ItemCardapio.builder()
        .id(1L)
        .nome("  ")
        .preco(new BigDecimal("5.00"))
        .build();

    Set<ConstraintViolation<ItemCardapio>> v1 = VALIDATOR.validate(invalidoNome);
    assertTrue(temViolacao(v1, "nome", "nome é obrigatório"));

    // preco nulo -> @NotNull
    ItemCardapio invalidoPrecoNulo = ItemCardapio.builder()
        .nome("Refrigerante")
        .preco(null)
        .build();

    Set<ConstraintViolation<ItemCardapio>> v2 = VALIDATOR.validate(invalidoPrecoNulo);
    assertTrue(temViolacao(v2, "preco", "preco é obrigatório"));

    // preco menor que 0.01 -> @DecimalMin
    ItemCardapio invalidoPrecoMin = ItemCardapio.builder()
        .nome("Água")
        .preco(new BigDecimal("0.00"))
        .build();

    Set<ConstraintViolation<ItemCardapio>> v3 = VALIDATOR.validate(invalidoPrecoMin);
    assertTrue(temViolacao(v3, "preco", "preco deve ser maior que zero"));
  }

  private static boolean temViolacao(Set<ConstraintViolation<ItemCardapio>> violacoes,
                                     String campo, String mensagem) {
    return violacoes.stream().anyMatch(v ->
        campo.equals(v.getPropertyPath().toString()) &&
        mensagem.equals(v.getMessage()));
  }
}
