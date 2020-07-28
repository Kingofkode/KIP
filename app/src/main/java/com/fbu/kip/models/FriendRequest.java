package com.fbu.kip.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("FriendRequest")
public class FriendRequest extends ParseObject {

  public static final String KEY_SENDER_ID = "senderID";
  public static final String KEY_RECIPIENT_ID = "recipientID";

  public ParseUser getSender() { return getParseUser(KEY_SENDER_ID); }
  public void setSender(ParseUser sender) { put(KEY_SENDER_ID, sender); }

  public ParseUser getRecipient() { return getParseUser(KEY_RECIPIENT_ID); }
  public void setRecipient(ParseUser recipient) { put(KEY_RECIPIENT_ID, recipient); }
}
