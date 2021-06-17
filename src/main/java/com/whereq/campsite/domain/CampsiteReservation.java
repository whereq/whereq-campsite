package com.whereq.campsite.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_campsite_reservation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CampsiteReservation implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "campsite_id")
  private Campsite campsite;

  // yyyyMMdd
  @Column(name = "start_date")
  private Integer startDate;

  // yyyyMMdd
  @Column(name = "end_date")
  private Integer endDate;

  @Column(name = "updated_timestamp")
  private LocalDateTime updatedTimestamp;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Campsite getCampsite() {
    return campsite;
  }

  public void setCampsite(Campsite campsite) {
    this.campsite = campsite;
  }

  public Integer getStartDate() {
    return startDate;
  }

  public void setStartDate(Integer startDate) {
    this.startDate = startDate;
  }

  public Integer getEndDate() {
    return endDate;
  }

  public void setEndDate(Integer endDate) {
    this.endDate = endDate;
  }

  public LocalDateTime getUpdatedTimestamp() {
    return updatedTimestamp;
  }

  public void setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
    this.updatedTimestamp = updatedTimestamp;
  }
}
