package com.example.kip.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Friendship")
public class Friendship extends ParseObject {
  public static final String KEY_USER_A = "userA";
  public static final String KEY_USER_B = "userB";

  public ParseUser getUserA() { return getParseUser(KEY_USER_A); }
  public void setUserA(ParseUser userA) { put(KEY_USER_A, userA); }

  public ParseUser getUserB() { return getParseUser(KEY_USER_B); }
  public void setUserB(ParseUser userB) { put(KEY_USER_B, userB); }
}
