package com.example.kip;

import android.content.Context;

import com.example.kip.models.Conversation;
import com.example.kip.models.Message;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public abstract class ParseTest {

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

}