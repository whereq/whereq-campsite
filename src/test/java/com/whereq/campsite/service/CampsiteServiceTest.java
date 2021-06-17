package com.whereq.campsite.service;

import com.whereq.campsite.domain.Campsite;
import com.whereq.campsite.domain.CampsiteReservation;
import com.whereq.campsite.domain.User;
import com.whereq.campsite.model.CampsiteVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CampsiteServiceTest {

  @Autowired
  CampsiteService campsiteService;

  @BeforeEach
  public void init() {
    campsiteService.syncReservationAndAvailability();
  }

  @Test
  public void testReserveCampsite() {
    User user = new User();
    user.setId(1L);

    Campsite campsite = new Campsite();
    campsite.setId(1L);

    Integer startDateInNum = 20210610;
    Integer span = 3;

    CampsiteReservation campsiteReservation = campsiteService.reserveCampsite(user, campsite, startDateInNum, span);
    Assertions.assertNull(campsiteReservation);

    startDateInNum = 20210625;
    span = 3;

    campsiteReservation = campsiteService.reserveCampsite(user, campsite, startDateInNum, span);
    Assertions.assertNotNull(campsiteReservation);
  }

  @Test
  public void testReserveCampsiteWithOverMaxReservationDays() {
    User user = new User();
    user.setId(1L);

    Campsite campsite = new Campsite();
    campsite.setId(1L);

    Integer startDateInNum = 20210610;
    Integer span = 4;

    Exception exception =
        Assertions.assertThrows(Exception.class,
            () -> {
              campsiteService.reserveCampsite(user, campsite, startDateInNum, span);
            });

    Assertions.assertTrue(exception.getMessage().contains("The campsite can not be reserved for more than 3 days"));
  }

  @Disabled("Do not run this test case since the unknown user will be automatically added to campsite!")
  @Test
  public void testReserveCampsiteWithUnknownUser() {
    User user = new User();
    user.setId(10000L);

    Campsite campsite = new Campsite();
    campsite.setId(1L);

    Integer startDateInNum = 20210625;
    Integer span = 2;

    Exception exception =
        Assertions.assertThrows(Exception.class,
            () -> {
              campsiteService.reserveCampsite(user, campsite, startDateInNum, span);
            });

    Assertions.assertTrue(exception.getMessage().contains("org.hibernate.exception.ConstraintViolationException: could not execute statement"));
  }

  @Test
  public void testFindAvailableCampsites() {
    Integer startDateInNum = 20210606;
    Integer span = 30;
    List<CampsiteVO> campsiteVOList = campsiteService.findAvailableCampsites(startDateInNum, span);
    System.out.println(campsiteVOList);
  }

  @Test
  public void testFindCampsiteAvailability() {
    campsiteService.syncReservationAndAvailability();

    Integer startDateInNum = 20210630;
    Integer span = 3;
    CampsiteVO campsiteVO = campsiteService.findCampsiteAvailability(1L, startDateInNum, span);
    System.out.println(campsiteVO);

    startDateInNum = 20210501;
    span = 2;
    campsiteVO = campsiteService.findCampsiteAvailability(1L, startDateInNum, span);
    System.out.println(campsiteVO);
  }
}
