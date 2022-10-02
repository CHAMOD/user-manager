package com.user.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
  private long id;
  private String name;
  private String mobileNo;
  private String email;

  public User() {
  }

  public User(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public User(final long id, final String name, final String mobileNo, final String email) {
    this.id = id;
    this.name = name;
    this.mobileNo = mobileNo;
    this.email = email;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Column(name = "name", nullable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "mobile_no", nullable = false)
  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(final String mobileNo) {
    this.mobileNo = mobileNo;
  }

  @Column(name = "email", nullable = false)
  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", name='" + name + '\'' +
      '}';
  }
}
