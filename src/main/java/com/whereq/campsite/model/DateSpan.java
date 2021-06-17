package com.whereq.campsite.model;

import java.io.Serializable;
import java.time.LocalDate;

public class DateSpan implements Serializable {

  private static final long serialVersionUID = 1L;

  LocalDate startDate;
  LocalDate endDate;

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }
}
