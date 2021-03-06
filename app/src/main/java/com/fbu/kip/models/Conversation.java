package com.fbu.kip.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Conversation")
public class Conversation extends ParseObject {
  public static final String KEY_MEMBER_IDS = "memberIDs";
  public static final String KEY_LAST_MESSAGE = "lastMessage";
  public static final String KEY_CREATED_AT = "createdAt";
  public static final String KEY_UPDATED_AT = "updatedAt";

  public List<ParseUser> getMembers() { return (List<ParseUser>) get(KEY_MEMBER_IDS); }
  public void setMembers(List<ParseUser> members) { put(KEY_MEMBER_IDS, members); }

  public Message getLastMessage() { return (Message) get(KEY_LAST_MESSAGE); }
  public void setLastMessage(Message lastMessage) { put(KEY_LAST_MESSAGE, lastMessage); }

}
