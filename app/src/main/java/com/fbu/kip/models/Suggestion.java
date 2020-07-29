package com.fbu.kip.models;

import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel
public class Suggestion {
  ParseUser recipient;
  String suggestion;

  public Suggestion(ParseUser recipient, String suggestion) {
    this.recipient = recipient;
    this.suggestion = suggestion;
  }

  // Required by Parcel
  public Suggestion() {}

  public ParseUser getRecipient() {
    return recipient;
  }

  public void setRecipient(ParseUser recipient) {
    this.recipient = recipient;
  }

  public String getSuggestion() {
    return suggestion;
  }

  public void setSuggestion(String suggestion) {
    this.suggestion = suggestion;
  }
}
