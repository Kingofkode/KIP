package com.example.kip;

import android.app.Application;

import com.example.kip.models.Conversation;
import com.example.kip.models.Message;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    registerParseModels();

    // set applicationId, and server server based on the values in the Heroku settings.
    // clientKey is not needed unless explicitly configured
    // any network interceptors must be added with the Configuration Builder given this syntax
    Parse.initialize(new Parse.Configuration.Builder(this)
      .applicationId("kip-fbu") // should correspond to APP_ID env variable
      .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
      .server("https://kip-fbu.herokuapp.com/parse").build());
  }

  private void registerParseModels() {
     ParseObject.registerSubclass(Message.class);
     ParseObject.registerSubclass(Conversation.class);
  }
}