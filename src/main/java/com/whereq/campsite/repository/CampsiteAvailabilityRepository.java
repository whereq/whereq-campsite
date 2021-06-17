package com.whereq.campsite.repository;

import com.whereq.campsite.domain.CampsiteAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CampsiteAvailabilityRepository extends JpaRepository<CampsiteAvailability, Long> {

  @Query("SELECT ca FROM CampsiteAvailability ca WHERE ca.campsite.id = ?1")
  CampsiteAvailability findByCampsiteId(Long campsiteId);
}
