package com.whereq.campsite.utils;


import com.whereq.campsite.exception.GeneralValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class DateConverterTest {

  @Test
  public void testIntergerToLocalDate() {
    Integer dateInNum = 20210601;
    LocalDate localDate = DateConverter.integer2LocalDate(dateInNum);
    Assertions.assertNotNull(localDate);

    dateInNum = 20211301;
    try {
      localDate = DateConverter.integer2LocalDate(dateInNum);
    } catch (Exception e) {
      Assertions.assertTrue(e instanceof GeneralValidationException);
    }
  }

}
