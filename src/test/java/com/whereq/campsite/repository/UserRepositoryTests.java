package com.whereq.campsite.repository;

import com.whereq.campsite.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTests {

  @Autowired
  UserRepository userRepository;

  @Test
  public void testFindOneByEmailIgnoreCase() {
    String email = "foo.bar@email.com";
    User user = userRepository.findOneByEmailIgnoreCase(email).get();
    Assertions.assertNotNull(user);
  }

}
