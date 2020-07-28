package com.fbu.kip;

import com.fbu.kip.models.Friendship;
import com.parse.ParseUser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FriendshipTests extends ParseTest {

  // Tests getUserA() and setUserA()
  @Test
  public void userAIsCorrect() {
    Friendship friendship = new Friendship();

    // Mocked user
    ParseUser edwin = new ParseUser();
    edwin.setUsername("edwin");

    // Set userA to mocked user
    friendship.setUserA(edwin);

    // Verify if user A of friendship is edwin
    assertEquals("edwin", friendship.getUserA().getUsername());
  }

  // Tests getUserB() and setUserB()
  @Test
  public void userBIsCorrect() {
    Friendship friendship = new Friendship();

    // Mocked user
    ParseUser isaiah = new ParseUser();
    isaiah.setUsername("isaiah");

    // Set userB to mocked user
    friendship.setUserB(isaiah);

    // Verify if user B of friendship is isaiah
    assertEquals("isaiah", friendship.getUserB().getUsername());
  }
}
