package com.user.api.services;


import com.user.api.dtos.UserDto;
import com.user.api.entity.User;
import com.user.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

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

    return userRepository.save(user);
  }
}
