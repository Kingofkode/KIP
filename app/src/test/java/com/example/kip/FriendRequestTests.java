package com.example.kip;

import com.example.kip.models.FriendRequest;
import com.parse.ParseUser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FriendRequestTests extends ParseTest {

  // Tests getSender() and setSender()
  @Test
  public void senderIsCorrect() {
    FriendRequest friendRequest = new FriendRequest();

    // Mocked user
    ParseUser sender = new ParseUser();
    sender.setUsername("noel");

    // Set sender to mocked user
    friendRequest.setSender(sender);

    // Confirm if friendRequest's sender is indeed the mocked user
    assertEquals("noel", friendRequest.getSender().getUsername());
  }

  // Tests getRecipient() and setRecipient()
  @Test
  public void recipientIsCorrect() {
    FriendRequest friendRequest = new FriendRequest();

    // Mocked user
    ParseUser recipient = new ParseUser();
    recipient.setUsername("isaiah");

    // Set recipient to mocked user
    friendRequest.setRecipient(recipient);

    // Confirm if friendRequest's recipient is indeed the mocked user
    assertEquals("isaiah", friendRequest.getRecipient().getUsername());
  }
}
