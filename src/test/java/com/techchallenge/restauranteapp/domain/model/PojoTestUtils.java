package com.techchallenge.restauranteapp.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class PojoTestUtils {
  private PojoTestUtils() {}

  /** Verifica equals/hashCode (reflexivo, simétrico) e toString não vazio. */
  public static <T> void verifyEqualsHashCodeToString(T a, T sameState, T different) {
    // reflexivo
    assertEquals(a, a);
    assertEquals(a.hashCode(), a.hashCode());

    // simétrico / mesmo estado
    assertEquals(a, sameState);
    assertEquals(sameState, a);
    assertEquals(a.hashCode(), sameState.hashCode());

    // diferente
    if (different != null) {
      assertNotEquals(a, different);
      assertNotEquals(different, a);
    }

    // contra null e outro tipo
    assertNotEquals(a, null);
    assertNotEquals(a, new Object());

    // toString
    String s = String.valueOf(a);
    assertNotNull(s);
    assertFalse(s.isBlank(), "toString() não deveria ser vazio");
  }
}
