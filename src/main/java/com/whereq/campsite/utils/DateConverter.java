package com.whereq.campsite.utils;

import com.whereq.campsite.exception.GeneralValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverter {

  /**
   * Convert integer to LocalDate
   * @param dateInNum pattern yyyyMMdd
   * @return
   */
  public static LocalDate integer2LocalDate(Integer dateInNum) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    try {
      return LocalDate.parse(Integer.toString(dateInNum), dateTimeFormatter);
    } catch (Exception e) {
      throw new GeneralValidationException(e.getMessage());
    }
  }

  /**
   * Convert LocalDate object to integer
   * @param localDate pattern yyyyMMdd
   * @return
   */
  public static Integer localDate2Integer(LocalDate localDate) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    return Integer.parseInt(dateTimeFormatter.format(localDate));
  }
}
