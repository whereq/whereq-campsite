package com.whereq.campsite.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_campsite_availability")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CampsiteAvailability implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @JsonBackReference
  @OneToOne
  @JoinColumn(name = "campsite_id")
  private Campsite campsite;

  @Column(name = "availability")
  private String availability;

  @Column(name = "updated_timestamp")
  private LocalDateTime updatedTimestamp;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Campsite getCampsite() {
    return campsite;
  }

  public void setCampsite(Campsite campsite) {
    this.campsite = campsite;
  }

  public String getAvailability() {
    return availability;
  }

  public void setAvailability(String availability) {
    this.availability = availability;
  }

  public LocalDateTime getUpdatedTimestamp() {
    return updatedTimestamp;
  }

  public void setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
    this.updatedTimestamp = updatedTimestamp;
  }
}
