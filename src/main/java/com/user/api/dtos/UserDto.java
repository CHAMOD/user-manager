package com.user.api.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserDto {

  public UserDto(final String name, final String mobileNo, final String email) {
    this.name = name;
    this.mobileNo = mobileNo;
    this.email = email;
  }

  @NotBlank(message = "name can't be blank")
  private String name;

  @NotBlank(message = "mobile number can't be blank")
  private String mobileNo;

  @NotBlank(message = "email can't be blank")
  @Email(message = "invalid format")
  private String email;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(final String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }
}
