package com.example.kip;

import android.content.Context;

import com.parse.Parse;
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

      ParseApplication.registerParseModels();
      ParseApplication.initializeParse(context);

      ParseUser.enableAutomaticUser();

      parseInit = true;
    }
  }

}