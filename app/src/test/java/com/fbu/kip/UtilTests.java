package com.fbu.kip;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTests extends ParseTest {
  @Test
  public void getElapsedDays() {
    // Edge case: null inputs
    long nullInputs = Utils.getElapsedDays(null, null);
    assertEquals(nullInputs, -1);

  }
}
