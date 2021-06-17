package com.whereq.campsite.repository;

import com.whereq.campsite.domain.Campsite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampsiteRepository extends JpaRepository<Campsite, Long> {
  List<Campsite> findFullyAvailableCampsite(Integer start, Integer span);
}
