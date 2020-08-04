package com.fbu.kip;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class UtilTests extends ParseTest {
  @Test
  public void getElapsedDays() {
    // Edge case: null inputs
    long nullInputs = Utils.getElapsedDays(null, null);
    assertEquals(nullInputs, -1);

    Date today = Calendar.getInstance().getTime();
    Calendar testCalendar = Calendar.getInstance();
    // Test date = today, should return 0 days elapsed
    assertEquals(0, Utils.getElapsedDays(today, testCalendar.getTime()));

    // Test date = yesterday, should return 1 day elapsed
    testCalendar.add(Calendar.DAY_OF_YEAR, -1);
    assertEquals(1, Utils.getElapsedDays(today, testCalendar.getTime()));

    // Test date = tomorrow, should return -1 days elapsed
    assertEquals(-1, Utils.getElapsedDays(testCalendar.getTime(), today));

    // Test date = 7 days ago, should return 7 days elapsed
    testCalendar.add(Calendar.DAY_OF_YEAR, -6);
    assertEquals(7, Utils.getElapsedDays(today, testCalendar.getTime()));

    // Test date = 7 days in future, should return -7 days elapse
    assertEquals(-7, Utils.getElapsedDays(testCalendar.getTime(), today));
  }
}
