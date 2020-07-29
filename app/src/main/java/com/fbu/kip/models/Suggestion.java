package com.fbu.kip.models;

import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel
public class Suggestion {
  ParseUser recipient;
  String body;

  public Suggestion(ParseUser recipient, String body) {
    this.recipient = recipient;
    this.body = body;
  }

  // Required by Parcel
  public Suggestion() {}

  public ParseUser getRecipient() {
    return recipient;
  }

  public void setRecipient(ParseUser recipient) {
    this.recipient = recipient;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
