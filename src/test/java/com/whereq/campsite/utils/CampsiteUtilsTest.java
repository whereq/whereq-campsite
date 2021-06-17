package com.whereq.campsite.utils;


import com.whereq.campsite.model.DateSpan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class CampsiteUtilsTest {

  @Test
  public void testCalculateCampsiteAvailability() {

    String campsiteAvailability = CampsiteUtils.alignCampsiteAvailability(366, '0');
    String ca = CampsiteUtils.calculateCampsiteAvailability(campsiteAvailability, 20210102, 3, '1');
    Assertions.assertEquals(366, ca.length());
    Assertions.assertEquals("111", ca.substring(1, 4));

    ca = CampsiteUtils.calculateCampsiteAvailability(campsiteAvailability, 20211230, 3, '1');
    Assertions.assertEquals(366, ca.length());
    Assertions.assertEquals("1", ca.substring(0, 1));
    Assertions.assertEquals("11", ca.substring(363, 365));
  }

  @Test
  public void testGetCampsiteAvailabilitySpan() {
    String campsiteAvailability = CampsiteUtils.alignCampsiteAvailability(366, '0');
    String ca = CampsiteUtils.getCampsiteAvailabilitySpan(campsiteAvailability, 20210102, 3);
    Assertions.assertEquals(3, ca.length());
    Assertions.assertEquals("000", ca);

    ca = CampsiteUtils.getCampsiteAvailabilitySpan(campsiteAvailability, 20211220, 30);
    Assertions.assertEquals(30, ca.length());
    Assertions.assertEquals(CampsiteUtils.alignCampsiteAvailability(30, '0'), ca);
  }


  @Test
  public void testCalculateDateSpan() {
    String ca = "111000110000000110";
    Set<DateSpan> dateSpans = CampsiteUtils.calculateDateSpan(ca, 20210606);
    System.out.println(dateSpans);
  }

}
