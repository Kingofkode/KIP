package com.example.kip;

import com.example.kip.models.Conversation;
import com.example.kip.models.Message;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class MessageTests extends ParseTest {

  // Tests getBody() and setBody()
  @Test
  public void bodyIsCorrect() {
    Message message = new Message();
    message.setBody("Hi from Menlo Park!");
    assertEquals("Hi from Menlo Park!", message.getBody());
  }

  // Tests getSender() and setSender()
  @Test
  public void senderIsCorrect() {
    Message message = new Message();

    // Mocked user
    ParseUser sender = new ParseUser();
    sender.setUsername("Mark");

    // Set sender to mocked user
    message.setSender(sender);

    // Check if Message's sender is indeed the mocked user
    assertEquals("Mark", message.getSender().getUsername());
  }

  // Tests getImage() and setImage()
  @Test
  public void imageIsCorrect() {
    Message message = new Message();

    // Mocked photo file
    ParseFile imageFile = new ParseFile("photo.jpg", null);

    // Set image to mocked photo
    message.setImage(imageFile);

    // Check if Message's image is indeed the mocked image
    assertEquals("photo.jpg", message.getImage().getName());
  }

  // Tests getConversation() and setConversation()
  @Test
  public void conversationIsCorrect() {
    // Mocked conversation with ID = 4
    Conversation conversation = new Conversation();
    conversation.setObjectId("4");

    Message message = new Message();

    // Set Message's conversation to the mocked conversation
    message.setConversation(conversation);

    // Check if Message's conversation is indeed the mocked conversation
    assertEquals("4", message.getConversation().getObjectId());
  }
}