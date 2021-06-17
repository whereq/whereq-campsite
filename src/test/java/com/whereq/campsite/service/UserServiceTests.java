package com.whereq.campsite.service;


import com.whereq.campsite.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

  @Autowired
  UserService userService;

  @Test
  public void testFindUserByEmail() {
    String email = "foo.bar@email.com";
    User user = userService.getUserByEmail(email);
    Assertions.assertNotNull(user);
  }

  @Test
  public void testFindNoneUserByEmail() {
    String email = "a.b@email.com";
    User user = userService.getUserByEmail(email);
    Assertions.assertNull(user);
  }
}
