package com.whereq.campsite.web.rest;

import com.whereq.campsite.exception.GeneralValidationException;
import com.whereq.campsite.domain.User;
import com.whereq.campsite.model.UserVO;
import com.whereq.campsite.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value = "User")
public class UserResource {

  @Autowired
  UserService userService;

  @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<UserVO> createUser(@RequestBody UserVO user) throws Exception {
    if (user.getId() != null) {
      throw new GeneralValidationException("A new user cannot already have an ID.");
    } else {
      user = userService.createUser(user);
      return ResponseEntity.created(new URI("/api/users" + user.getId()))
          .body(user);
    }
  }

  @GetMapping("/users")
  public ResponseEntity<List<User>> getAllUsers() {
    final List<User> userList = userService.getAllUsers();
    return new ResponseEntity<>(userList, HttpStatus.OK);
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long userId) {
    User user = userService.getUser(userId);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }
}
