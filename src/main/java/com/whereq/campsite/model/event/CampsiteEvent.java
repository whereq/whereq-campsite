package com.whereq.campsite.model.event;

import com.whereq.campsite.domain.CampsiteReservation;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CampsiteEvent implements Serializable {

  private Long seq;
  private Long reservationId;
  private Long userId;
  private Long campsiteId;
  private Integer startDate;
  private Integer span;
  private String action; //book, cancel
  private String state; //sent, completed
  private String status; //success, fail
  private String reason;
  private LocalDateTime eventTimestamp;
  private CampsiteReservation campsiteReservation;

  public Long getSeq() {
    return seq;
  }

  public void setSeq(Long seq) {
    this.seq = seq;
  }

  public Long getReservationId() {
    return reservationId;
  }

  public void setReservationId(Long reservationId) {
    this.reservationId = reservationId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getCampsiteId() {
    return campsiteId;
  }

  public void setCampsiteId(Long campsiteId) {
    this.campsiteId = campsiteId;
  }

  public Integer getStartDate() {
    return startDate;
  }

  public void setStartDate(Integer startDate) {
    this.startDate = startDate;
  }

  public Integer getSpan() {
    return span;
  }

  public void setSpan(Integer span) {
    this.span = span;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public LocalDateTime getEventTimestamp() {
    return eventTimestamp;
  }

  public void setEventTimestamp(LocalDateTime eventTimestamp) {
    this.eventTimestamp = eventTimestamp;
  }

  public CampsiteReservation getCampsiteReservation() {
    return campsiteReservation;
  }

  public void setCampsiteReservation(CampsiteReservation campsiteReservation) {
    this.campsiteReservation = campsiteReservation;
  }
}
