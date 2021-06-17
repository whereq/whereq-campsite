package com.whereq.campsite.repository;

import com.whereq.campsite.domain.CampsiteReservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CampsiteReservationRepositoryTests {

  @Autowired
  CampsiteReservationRepository campsiteReservationRepository;

  @Test
  public void testFindByCampsiteId() {
    List<CampsiteReservation> campsiteReservationList = campsiteReservationRepository.findByCampsiteId(100L);
    Assertions.assertNotNull(campsiteReservationList);
  }

  @Test
  public void testFindByDate() {
    Integer startDate = 20210615;
    Integer endDate = 20210618;
    List<CampsiteReservation> campsiteReservationList = campsiteReservationRepository.findByDate(startDate, endDate);
    Assertions.assertNotNull(campsiteReservationList);
  }

  @Test
  public void testFindCampsiteReservationByDate() {
    Long campsiteId = 1L;
    Integer startDate = 20210610;
    Integer endDate = 20210612;
    List<CampsiteReservation> campsiteReservationList = campsiteReservationRepository.findCampsiteByDate(campsiteId, startDate, endDate);
    Assertions.assertEquals(1, campsiteReservationList.size());

    startDate = 20210901;
    endDate = 20210902;
    campsiteReservationList = campsiteReservationRepository.findCampsiteByDate(campsiteId, startDate, endDate);
    Assertions.assertEquals(0, campsiteReservationList.size());
  }
}
