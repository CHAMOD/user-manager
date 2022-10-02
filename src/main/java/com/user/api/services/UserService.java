package com.user.api.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.user.api.dtos.UserDto;
import com.user.api.entity.User;
import com.user.api.exceptions.ResourceNotFoundException;
import com.user.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);
  @Autowired
  private UserRepository userRepository;

  public List<User> getAllRegisteredUsers() {
    return userRepository.findAll();
  }

  public User addUser(UserDto userDto) {
    User user = new User();
    user.setName(userDto.getName());
    user.setEmail(userDto.getEmail());
    user.setMobileNo(userDto.getMobileNo());

    logger.info("user is inserted: ", user);
    return userRepository.save(user);
  }

  public ResponseEntity<User> getUserById(final Long userId) throws ResourceNotFoundException {

    User user = userRepository.findById(userId).orElseThrow(() -> {
      logger.warn("user {} is not found", userId);
      return new ResourceNotFoundException("user is not found with id =" + userId);
    });

    return new ResponseEntity<>(user, HttpStatus.OK);
  }
}
