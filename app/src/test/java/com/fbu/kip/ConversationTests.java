package com.fbu.kip;

import com.fbu.kip.models.Conversation;
import com.fbu.kip.models.Message;
import com.parse.ParseUser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConversationTests extends ParseTest {

  // Tests getMembers() and setMembers()
  @Test
  public void membersIsCorrect() {
    Conversation conversation = new Conversation();

    // Mocked users
    ParseUser member1 = new ParseUser();
    member1.setUsername("Isaiah");
    ParseUser member2 = new ParseUser();
    member2.setUsername("Edwin");

    List<ParseUser> members = new ArrayList<>();
    members.add(member1);
    members.add(member2);

    // Set members to mocked users
    conversation.setMembers(members);

    // Check if Conversation's members are indeed the mocked users
    assertEquals("Isaiah", conversation.getMembers().get(0).getUsername());
    assertEquals("Edwin", conversation.getMembers().get(1).getUsername());
  }

  // Tests getLastMessage() and setLastMessage()
  @Test
  public void lastMessageIsCorrect() {
    Conversation conversation = new Conversation();

    // Mocked last message
    Message message = new Message();
    message.setBody("See you then!");

    // Set last message to mocked message
    conversation.setLastMessage(message);

    // Check if Conversation's lastMessage is indeed the mocked message
    assertEquals("See you then!", conversation.getLastMessage().getBody());
  }
}
