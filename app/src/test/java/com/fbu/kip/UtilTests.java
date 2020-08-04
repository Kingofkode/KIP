package com.fbu.kip;

import com.fbu.kip.activities.LoginActivity;
import com.parse.ParseUser;

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

    // Edge case: null input, should return been forever
    assertEquals(Utils.FriendshipStatus.beenForever, Utils.getFriendshipStatus(null));

    // Edge case: date in future, should return in touch
    testCalendar.add(Calendar.DAY_OF_YEAR, 1);
    assertEquals(Utils.FriendshipStatus.inTouch, Utils.getFriendshipStatus(testCalendar.getTime()));

    // Test date = 1 day ago, should return inTouch
    testCalendar.add(Calendar.DAY_OF_YEAR, -2);
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

  @Test
  public void getFullName() {
    // Edge case: null user, should return empty string
    assertEquals("", Utils.getFullName(null));

    // Test: user that doesn't have a full name. Should return the username
    ParseUser testUser = new ParseUser();
    testUser.setUsername("cornerVillage");
    assertEquals("cornerVillage", Utils.getFullName(testUser));

    // Test: user that has a username and a full name
    testUser.put(LoginActivity.FULL_NAME, "Isaiah Suarez");
    assertEquals("Isaiah Suarez", Utils.getFullName(testUser));
  }

  @Test
  public void getFriendshipTimestamp() {
    // Edge case: null date, should return empty string
    assertEquals("", Utils.getFriendshipTimestamp(null));

    // Test: day MJ won his 6th championship with the Bulls: June 14, 1998
    Calendar testCalendar = Calendar.getInstance();
    testCalendar.set(Calendar.MONTH, Calendar.JUNE);
    testCalendar.set(Calendar.DAY_OF_MONTH, 14);
    testCalendar.set(Calendar.YEAR, 1998);
    assertEquals("June 14, 1998", Utils.getFriendshipTimestamp(testCalendar.getTime()));

    // Test: Billie Eilish's 21st birthday
    testCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
    testCalendar.set(Calendar.DAY_OF_MONTH, 18);
    testCalendar.set(Calendar.YEAR, 2022);
    assertEquals("December 18, 2022", Utils.getFriendshipTimestamp(testCalendar.getTime()));
  }


}
