package com.user.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.api.config.CustomAuthenticationEntryPoint;
import com.user.api.dtos.UserDto;
import com.user.api.entity.User;
import com.user.api.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest extends Auth {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


  private final User RECORD_1 = new User(1, "Test User1", "0711234567", "test1@gmail.com");
  private final User RECORD_2 = new User(2, "Test User2", "0711235555", "test2@gmail.com");
  private final User RECORD_3 = new User(3, "Test User3", "0711234432", "test3@gmail.com");


  @Test
  public void whenCreateUser_thenReturnSavedUser() throws Exception {
    setAuthentication("user", "password", session);

    UserDto userToSave = new UserDto("Test User1", "0711234567", "test1@gmail.com");

    when(userService.addUser(any(UserDto.class))).thenReturn(RECORD_1);

    mockMvc.perform(post("/v1/users/register")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(userToSave)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name",
                          is(RECORD_1.getName())))
      .andExpect(jsonPath("$.email",
                          is(RECORD_1.getEmail())))
      .andExpect(jsonPath("$.mobileNo",
                          is(RECORD_1.getMobileNo())))
      .andDo(print());

    verify(userService).addUser(any(UserDto.class));

  }


  // positive scenario - valid user id
  // JUnit test for GET user by id REST API
  @Test
  public void givenUserId_whenGetUserById_thenReturnUserObject() throws Exception {
    setAuthentication("admin", "password", session);
    // given - precondition or setup
    long userId = 1L;

    when(userService.getUserById(userId)).thenReturn(new ResponseEntity<>(RECORD_1, HttpStatus.OK));

    ResultActions response = mockMvc.perform(get("/v1/users/userProfile/{id}", userId));

    // then - verify the output
    response.andExpect(status().isOk())
      .andDo(print())
      .andExpect(jsonPath("$.name", is(RECORD_1.getName())))
      .andExpect(jsonPath("$.email", is(RECORD_1.getEmail())))
      .andExpect(jsonPath("$.mobileNo", is(RECORD_1.getMobileNo())));

  }

  // JUnit test for Get All registered users REST API
  @Test
  public void givenListOfUsers_whenGetAllRegisteredUsers_thenReturnUsersList() throws Exception {
    setAuthentication("admin", "password", session);
    // given - precondition or setup
    List<User> listOfUsers = new ArrayList<>();
    listOfUsers.add(RECORD_1);
    listOfUsers.add(RECORD_2);
    listOfUsers.add(RECORD_3);
    given(userService.getAllRegisteredUsers()).willReturn(listOfUsers);

    ResultActions response = mockMvc.perform(get("/v1/users/registeredUsers"));

    // then - verify the output
    response.andExpect(status().isOk())
      .andDo(print())
      .andExpect(jsonPath("$.size()",
                          is(listOfUsers.size())));

  }


  @ParameterizedTest(name = "{0}")
  @MethodSource("dataToValidate")
  public void whenProvideIncompleteUserToCreateUser_thenReturnSavedUser(final String scenario,
                                                                        final UserDto inputData,
                                                                        final String errorMessage,
                                                                        final ResultMatcher resultMatcher,
                                                                        final boolean isPassed
                                                                       ) throws Exception {
    setAuthentication("user", "password", session);

    when(userService.addUser(any(UserDto.class))).thenReturn(RECORD_1);

    if (isPassed) {
      mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputData)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name",
                            is(RECORD_1.getName())))
        .andExpect(jsonPath("$.email",
                            is(RECORD_1.getEmail())))
        .andExpect(jsonPath("$.mobileNo",
                            is(RECORD_1.getMobileNo())))
        .andDo(print());
    } else {
      mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputData)))
        .andExpect(resultMatcher)
        .andExpect(jsonPath("$.message",
                            is(errorMessage)))
        .andDo(print());

    }


  }

  private static Stream<Arguments> dataToValidate() {


    return Stream.of(arguments("Validation fail when name is empty",
                               new UserDto("", "0711231234", "TestEmail@gmail.com"),
                               "[name can't be blank]",
                               status().isBadRequest(),
                               false
                              ),
                     arguments("Validation fail when email is empty",
                               new UserDto("Test", "0711231234", ""),
                               "[email can't be blank]",
                               status().isBadRequest(),
                               false
                              ),
                     arguments("Validation fail when email is invalid",
                               new UserDto("Test", "0711231234", "Test"),
                               "[invalid format]",
                               status().isBadRequest(),
                               false
                              ),
                     arguments("Validation fail when mobileNo is empty",
                               new UserDto("Test", "", "TestEmail@gmail.com"),
                               "[mobile number can't be blank]",
                               status().isBadRequest(),
                               false
                              ),
                     arguments("Validation pass when all details are correct",
                               new UserDto("TestName", "0711231234", "TestEmail@gmail.com"),
                               "",
                               status().isOk(),
                               true
                              )
                    );
  }

}
