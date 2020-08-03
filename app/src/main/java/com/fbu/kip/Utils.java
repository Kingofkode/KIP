package com.fbu.kip;

import com.fbu.kip.activities.LoginActivity;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

  public static String getConversationTimestamp(Date date) {
    String dateFormat = "h:mm aa";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
    simpleDateFormat.setLenient(true);
    return simpleDateFormat.format(date);
  }

  public static String getFriendshipTimestamp(Date date) {
    String dateFormat = "MMMM d, yyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
    simpleDateFormat.setLenient(true);
    return simpleDateFormat.format(date);
  }


  public static String getFullName(ParseUser user) {
    String fullName = (String) user.get(LoginActivity.FULL_NAME);
    if (fullName == null) {
      return user.getUsername();
    }
    return fullName;
  }

  public final static String[] suggestionMessages = {"Wanna grab lunch?", "Hey! It's been awhile. How've you been?", "How was that trip to Colorado?", "How'd the presentation go?", "How's post grad life?", "Want to go to Philz Coffee?"};
}
