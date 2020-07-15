package com.example.kip.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {

  public static final String KEY_BODY = "body";
  public static final String KEY_SENDER_ID = "senderID";
  public static final String KEY_IMAGE = "image";
  public static final String KEY_CONVERSATION_ID = "conversationID";
  public static final String KEY_CREATED_AT = "createdAt";
  public static final String KEY_UPDATED_AT = "updatedAt";

  public String getBody() { return getString(KEY_BODY); }
  public void setBody(String body) { put(KEY_BODY, body); }

  public ParseUser getSender() { return getParseUser(KEY_SENDER_ID); }
  public void setSender(ParseUser sender) { put(KEY_SENDER_ID, sender); }

  public ParseFile getImage() { return getParseFile(KEY_IMAGE); }
  public void setImage(ParseFile parseFile) { put(KEY_IMAGE, parseFile); }

  public Conversation getConversation() { return (Conversation) get(KEY_CONVERSATION_ID); }
  public void setConversation(Conversation conversation) { put(KEY_CONVERSATION_ID, conversation); }

}
