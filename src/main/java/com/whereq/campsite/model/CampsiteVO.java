package com.whereq.campsite.model;

import java.io.Serializable;
import java.util.Set;

public class CampsiteVO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String name;
  private Long popularity;
  private Long triedTimes;

  private Set<DateSpan> availabilities;
  private Set<DateSpan> reservations;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getPopularity() {
    return popularity;
  }

  public void setPopularity(Long popularity) {
    this.popularity = popularity;
  }

  public Long getTriedTimes() {
    return triedTimes;
  }

  public void setTriedTimes(Long triedTimes) {
    this.triedTimes = triedTimes;
  }

  public Set<DateSpan> getAvailabilities() {
    return availabilities;
  }

  public void setAvailabilities(Set<DateSpan> availabilities) {
    this.availabilities = availabilities;
  }

  public Set<DateSpan> getReservations() {
    return reservations;
  }

  public void setReservations(Set<DateSpan> reservations) {
    this.reservations = reservations;
  }
}
