package app.chinabank.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

  /**
   * Gets the timestamp with the system-default timezone.
   *
   * @return The current timestamp
   */
  public static Timestamp currentTimestamp() {
    long currentMilli = LocalDateTime.now(ZoneId.systemDefault()).toInstant(ZoneOffset.UTC)
        .toEpochMilli();

    return new Timestamp(currentMilli);
  }

  /**
   * Converts a date string to SQL Timestamp.
   *
   * @param string - date string to be converted
   * @return The converted timestamp
   */
  public static Timestamp toTimestamp(String string) {
    Date date = Date.valueOf(string);

    return new Timestamp(date.getTime());
  }

  /**
   * Formats a Timestamp with the pattern 'yyyy-MM-dd'.
   *
   * @param time - Timestamp to be formatted
   * @return String with a date pattern
   */
  public static String dateFormat(Timestamp time) {
    return time.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

  /**
   * Formats a Timestamp with the pattern 'yyyy-MM-dd hh:mm:ss'.
   *
   * @param time - Timestamp to be converted
   * @return String with a timestamp pattern
   */
  public static String timestampFormat(Timestamp time) {
    return time.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
  }

  /**
   * Formats a Timestamp with the given pattern.
   *
   * @param time - Timestamp to be converted
   * @param pattern - pattern to format the timestamp with
   * @return String with the given pattern
   */
  public static String formatTimestamp(Timestamp time, String pattern) {
    return time.toLocalDateTime().format(DateTimeFormatter.ofPattern(pattern));
  }

}
