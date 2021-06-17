package com.whereq.campsite.repository;

import com.whereq.campsite.domain.CampsiteReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the {@link CampsiteReservation} entity.
 */
@Repository
public interface CampsiteReservationRepository extends JpaRepository<CampsiteReservation, Long> {

  List<CampsiteReservation> findByCampsiteId(Long campsiteId);

  @Query("SELECT cr FROM CampsiteReservation cr WHERE" +
      " function('PARSEDATETIME', cr.startDate, 'yyyyMMdd') > function('PARSEDATETIME', ?2, 'yyyyMMdd') OR" +
      " function('PARSEDATETIME', cr.endDate, 'yyyyMMdd') < function('PARSEDATETIME', ?1, 'yyyyMMdd')"
  )
  List<CampsiteReservation> findByDate(Integer startDate, Integer endDate);

  @Query("SELECT cr FROM CampsiteReservation cr WHERE" +
      " cr.campsite.id = ?1 AND" +
      " ((cr.startDate <= ?3 AND cr.endDate >= ?3) OR " +
      "  (cr.startDate <= ?2 AND cr.endDate >= ?3) OR " +
      "  (cr.startDate <= ?2 AND cr.endDate >= ?2))"
  )
  List<CampsiteReservation> findCampsiteByDate(Long campsiteId, Integer startDate, Integer endDate);
}
