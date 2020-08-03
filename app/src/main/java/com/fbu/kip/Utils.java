package com.fbu.kip;

import com.fbu.kip.activities.LoginActivity;
import com.fbu.kip.models.Friendship;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

  public enum FriendshipStatus {
    inTouch,
    beenASecond,
    beenAMinute,
    beenAWhile,
    beenForever
  }

  public static FriendshipStatus getFriendshipStatus(Date lastMessageDate) {
    if (lastMessageDate == null)
      return FriendshipStatus.beenForever;

    long differenceInMili = Calendar.getInstance().getTime().getTime() - lastMessageDate.getTime();

    long secondsInMilli = 1000;
    long minutesInMilli = secondsInMilli * 60;
    long hoursInMilli = minutesInMilli * 60;
    long daysInMilli = hoursInMilli * 24;
    // Calculate elapsed time
    long elapsedDays = differenceInMili / daysInMilli;
    differenceInMili = differenceInMili % daysInMilli;

    long elapsedHours = differenceInMili / hoursInMilli;
    differenceInMili = differenceInMili % hoursInMilli;

    long elapsedMinutes = differenceInMili / minutesInMilli;
    differenceInMili = differenceInMili % minutesInMilli;

    long elapsedSeconds = differenceInMili / secondsInMilli;
    // Determine outcome
    if (elapsedDays < 1)
      return FriendshipStatus.inTouch;
    if (elapsedDays < 2)
      return FriendshipStatus.beenASecond;
    if (elapsedDays < 3)
      return FriendshipStatus.beenAMinute;
    if (elapsedDays < 4)
      return FriendshipStatus.beenAWhile;
    return FriendshipStatus.beenForever;
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
