package com.whereq.campsite.model;

import java.io.Serializable;

public class CampsiteReservationResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String email;
  private String firstName;
  private String lastName;
  private Long campsiteId;
  private String campsiteName;
  private Integer startDateInNum;
  private Integer endDateInNum;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Long getCampsiteId() {
    return campsiteId;
  }

  public void setCampsiteId(Long campsiteId) {
    this.campsiteId = campsiteId;
  }

  public String getCampsiteName() {
    return campsiteName;
  }

  public void setCampsiteName(String campsiteName) {
    this.campsiteName = campsiteName;
  }

  public Integer getStartDateInNum() {
    return startDateInNum;
  }

  public void setStartDateInNum(Integer startDateInNum) {
    this.startDateInNum = startDateInNum;
  }

  public Integer getEndDateInNum() {
    return endDateInNum;
  }

  public void setEndDateInNum(Integer endDateInNum) {
    this.endDateInNum = endDateInNum;
  }
}
