package com.whereq.campsite.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "t_campsite")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

@NamedNativeQueries({
    @NamedNativeQuery(
        name = "Campsite.findFullyAvailableCampsite",
        query = "SELECT t2.id, t2.name, t2.popularity, t2.tried_times " +
            " FROM t_campsite_availability t1, t_campsite t2 " +
            " WHERE CAST(SUBSTRING(t1.availability, ?1, ?2) AS INT) = 0" +
            " AND t1.campsite_id = t2.id",
        resultClass = Campsite.class
    )
})
public class Campsite implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(name = "name", length = 50)
  private String name;

  // The more the campsite gets booked or tried to book, the higher popularity it is
  @Column(name = "popularity")
  private Long popularity;

  @JsonManagedReference
  @OneToMany(
      mappedBy = "campsite"
  )
  private Set<CampsiteReservation> campsiteReservations;

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

  public Set<CampsiteReservation> getCampsiteReservations() {
    return campsiteReservations;
  }

  public void setCampsiteReservations(Set<CampsiteReservation> campsiteReservations) {
    this.campsiteReservations = campsiteReservations;
  }
}
