package com.example.kip;

import android.content.Context;

import com.example.kip.models.Conversation;
import com.example.kip.models.Message;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class KIPUnitTests {
  static boolean parseInit = false;

  @Before
  public void setup() {
    if (!parseInit) {
      Context context = ShadowApplication.getInstance().getApplicationContext();
      Parse.enableLocalDatastore(context);

      ParseObject.registerSubclass(Message.class);
      ParseObject.registerSubclass(Conversation.class);

      Parse.initialize(new Parse.Configuration.Builder(context)
        .applicationId("kip-fbu")
        .clientKey(null)
        .server("https://kip-fbu.herokuapp.com/parse").build());

      ParseUser.enableAutomaticUser();

      parseInit = true;
    }
  }

  // Tests getBody() and setBody()
  @Test
  public void message_body_isCorrect() {
    Message message = new Message();
    message.setBody("Hi from Menlo Park!");
    assertEquals("Hi from Menlo Park!", message.getBody());
  }

  // Tests getSender() and setSender()
  @Test
  public void message_sender_isCorrect() {
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
  public void message_image_isCorrect() {
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
  public void message_conversation_isCorrect() {
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