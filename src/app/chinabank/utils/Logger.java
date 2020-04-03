package app.chinabank.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {

  /**
   * Logs an exception message and where it occured.
   *
   * @param e - Exception to be logged.
   */
  public static void error(Exception e) {
    StackTraceElement stack = e.getStackTrace()[0];
    String            file  = stack.getFileName();
    int               line  = stack.getLineNumber();
    String            src   = "(" + file + ":" + line + ")";
    String            msg   = e.getMessage();

    System.out.println(timestamp() + "[ERROR]: " + msg + " at " + src);
  }

  /**
   * Logs an Object.toString() method.
   *
   * @param obj - Object to be logged.
   */
  public static void log(Object obj) {
    System.out.println(timestamp() + "[LOG]:   " + obj.toString());
  }

  private static String timestamp() {
    LocalTime now  = LocalTime.now();
    String    time = now.format(DateTimeFormatter.ofPattern("h:m:ss"));

    return "[" + time + "]";
  }

}
