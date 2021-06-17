package com.whereq.campsite.utils;

import com.whereq.campsite.domain.Campsite;
import com.whereq.campsite.domain.CampsiteAvailability;
import com.whereq.campsite.domain.CampsiteReservation;
import com.whereq.campsite.model.CampsiteReservationResponse;
import com.whereq.campsite.model.CampsiteVO;
import com.whereq.campsite.model.DateSpan;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CampsiteUtils {

  public static String alignCampsiteAvailability(int length, char value) {
    char[] chars = new char[length];
    Arrays.fill(chars, value);
    return new String(chars);
  }

  public static LocalDate lastDayOfYear() {
    return LocalDate.now().with(TemporalAdjusters.lastDayOfYear());
  }

  /**
   * From startDateInNum, turn 0 to 1 until the span, means the campsite is not available during the date span
   * @param ca
   * @param startDateInNum
   * @param span
   * @param flag
   * @return
   */
  public static String calculateCampsiteAvailability(String ca, Integer startDateInNum, Integer span, char flag) {
    String result = "";
    int startDayOfYear = DateConverter.integer2LocalDate(startDateInNum).getDayOfYear();
    int lastDayOfYear = lastDayOfYear().getDayOfYear();
    int beginIndex = 0;
    int endIndex = 0;

    if (lastDayOfYear - startDayOfYear >= span) {
      beginIndex = startDayOfYear - 1;
      endIndex = beginIndex + span;
      result = ca.substring(0, beginIndex) + alignCampsiteAvailability(span, '1') + ca.substring(endIndex);
    } else {
      beginIndex = startDayOfYear - 1;
      int len = lastDayOfYear - startDayOfYear + 1;
      endIndex = span - len;

      result = alignCampsiteAvailability(endIndex, '1') + ca.substring(endIndex, beginIndex);

      result += alignCampsiteAvailability(len, '1') + ca.substring(lastDayOfYear);
    }
    return result;
  }

  public static String getCampsiteAvailabilitySpan(String ca, Integer startDateInNum, Integer span) {
    String result = "";
    int startDayOfYear = DateConverter.integer2LocalDate(startDateInNum).getDayOfYear();
    int lastDayOfYear = lastDayOfYear().getDayOfYear();

    int beginIndex = 0;
    int endIndex = 0;

    if (lastDayOfYear - startDayOfYear >= span) {
      beginIndex = startDayOfYear - 1;
      endIndex = beginIndex + span;
      result = ca.substring(beginIndex, endIndex);
    } else {
      beginIndex = startDayOfYear - 1;
      int len = lastDayOfYear - startDayOfYear + 1;
      endIndex = span - len;
      result = ca.substring(beginIndex, lastDayOfYear) + ca.substring(0, endIndex);
    }
    return result;
  }

  public static Set<DateSpan> calculateDateSpan(String ca, Integer startDateInNum) {
    if (ca == null || !ca.contains("0")) {
      return null;
    }

    LocalDate startDate = DateConverter.integer2LocalDate(startDateInNum);
    Set<DateSpan> dateSpans = new LinkedHashSet<>();

    Pattern pattern = Pattern.compile("0+");
    Matcher matcher = pattern.matcher(ca);
    while (matcher.find()) {
      DateSpan dateSpan = new DateSpan();
      dateSpan.setStartDate(startDate.plusDays(matcher.start()));
      dateSpan.setEndDate(startDate.plusDays(matcher.end() - 1));
      dateSpans.add(dateSpan);
    }
    return dateSpans;
  }

  public static CampsiteVO campsiteAvailability2VO(CampsiteAvailability campsiteAvailability, Integer startDateInNum, Integer span) {
    if (campsiteAvailability == null) {
      return null;
    }

    CampsiteVO campsiteVO = null;
    String ca = CampsiteUtils.getCampsiteAvailabilitySpan(campsiteAvailability.getAvailability(), startDateInNum, span);
    Set<DateSpan> dateSpanSet = calculateDateSpan(ca, startDateInNum);
    if (!CollectionUtils.isEmpty(dateSpanSet)) { // Means this campsite is available during the given date span
      Campsite campsite = campsiteAvailability.getCampsite();
      campsiteVO = new CampsiteVO();
      BeanUtils.copyProperties(campsite, campsiteVO);
      campsiteVO.setAvailabilities(dateSpanSet);
    }
    return campsiteVO;
  }

  public static CampsiteVO campsiteReservation2VO(CampsiteReservation campsiteReservation) {
    if (campsiteReservation == null) {
      return null;
    }

    CampsiteVO campsiteVO = new CampsiteVO();
    BeanUtils.copyProperties(campsiteReservation.getCampsite(), campsiteVO);
    DateSpan dateSpan = new DateSpan();
    dateSpan.setStartDate(DateConverter.integer2LocalDate(campsiteReservation.getStartDate()));
    dateSpan.setEndDate(DateConverter.integer2LocalDate(campsiteReservation.getEndDate()));
    campsiteVO.setReservations(Collections.singleton(dateSpan));
    return campsiteVO;
  }

  public static CampsiteReservationResponse campsiteReservation2Response(CampsiteReservation campsiteReservation) {
    if (campsiteReservation == null) {
      return null;
    }

    CampsiteReservationResponse campsiteReservationResponse = new CampsiteReservationResponse();
    campsiteReservationResponse.setId(campsiteReservation.getId());
    campsiteReservationResponse.setEmail(campsiteReservation.getUser().getEmail());
    campsiteReservationResponse.setFirstName(campsiteReservation.getUser().getFirstName());
    campsiteReservationResponse.setLastName(campsiteReservation.getUser().getLastName());
    campsiteReservationResponse.setCampsiteId(campsiteReservation.getCampsite().getId());
    campsiteReservationResponse.setCampsiteName(campsiteReservation.getCampsite().getName());
    campsiteReservationResponse.setStartDateInNum(campsiteReservation.getStartDate());
    campsiteReservationResponse.setEndDateInNum(campsiteReservation.getEndDate());
    return campsiteReservationResponse;
  }
}
