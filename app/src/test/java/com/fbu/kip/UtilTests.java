package com.fbu.kip;

import com.fbu.kip.models.Friendship;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class UtilTests extends ParseTest {
  @Test
  public void getElapsedDays() {
    // Edge case: null inputs
    int nullInputs = Utils.getElapsedDays(null, null);
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

  @Test
  public void getFriendshipStatus() {
    Calendar testCalendar = Calendar.getInstance();

    // Edge case: null input
    assertEquals(Utils.FriendshipStatus.beenForever, Utils.getFriendshipStatus(null));

    // Test date = 1 day ago, should return inTouch
    testCalendar.add(Calendar.DAY_OF_YEAR, -1);
    assertEquals(Utils.FriendshipStatus.inTouch, Utils.getFriendshipStatus(testCalendar.getTime()));

    // Test date = 2 days ago, should return been a second
    testCalendar.add(Calendar.DAY_OF_YEAR, -1);
    assertEquals(Utils.FriendshipStatus.beenASecond, Utils.getFriendshipStatus(testCalendar.getTime()));

    // Test date = 3 days ago, should return been a minute
    testCalendar.add(Calendar.DAY_OF_YEAR, -1);
    assertEquals(Utils.FriendshipStatus.beenAMinute, Utils.getFriendshipStatus(testCalendar.getTime()));

    // Test date = 4 days ago, should return been a while
    testCalendar.add(Calendar.DAY_OF_YEAR, -1);
    assertEquals(Utils.FriendshipStatus.beenAWhile, Utils.getFriendshipStatus(testCalendar.getTime()));

    // Test date = 5 days ago, should return been forever
    testCalendar.add(Calendar.DAY_OF_YEAR, -1);
    assertEquals(Utils.FriendshipStatus.beenForever, Utils.getFriendshipStatus(testCalendar.getTime()));
  }

}
