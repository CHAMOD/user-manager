package com.user.api.controllers;

import com.user.api.dtos.UserDto;
import com.user.api.entity.User;
import com.user.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

  @Autowired
  UserService userService;

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/registeredUsers")
  public List<User> getAllUsers() {
    return userService.getAllRegisteredUsers();
  }


  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PostMapping("/register")
  public User createUser(@RequestBody @Valid UserDto user) {
    return userService.addUser(user);
  }

}
