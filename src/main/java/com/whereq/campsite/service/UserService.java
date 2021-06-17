package com.whereq.campsite.service;

import com.whereq.campsite.domain.User;
import com.whereq.campsite.model.UserVO;
import com.whereq.campsite.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  @Transactional
  public UserVO createUser(UserVO userVO) {
    User user = new User();
    BeanUtils.copyProperties(userVO, user);
    user = userRepository.save(user);
    userVO.setId(user.getId());
    return userVO;
  }

  @Transactional
  public User createUser(User user) {
    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Transactional(readOnly = true)
  public User getUserByEmail(String email) {
    return userRepository.findOneByEmailIgnoreCase(email).orElse(null);
  }

  @Transactional(readOnly = true)
  public User getUser(Long userId) {
    return userRepository.findById(userId).orElse(null);
  }
}
