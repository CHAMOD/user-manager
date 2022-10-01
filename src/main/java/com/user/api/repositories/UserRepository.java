package com.user.api.repositories;

import com.user.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findUsersByName(String userName);
}
