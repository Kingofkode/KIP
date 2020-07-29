package com.fbu.kip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

  public static String getTimestamp(Date date) {
    String dateFormat = "h:mm aa";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
    simpleDateFormat.setLenient(true);
    return simpleDateFormat.format(date);
  }
}