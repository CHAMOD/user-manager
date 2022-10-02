
package com.user.api.exceptions;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex, HttpHeaders headers,
    HttpStatus status, WebRequest request) {

    List<String> errors = ex.getBindingResult()
      .getFieldErrors()
      .stream()
      .map(x -> x.getDefaultMessage())
      .collect(Collectors.toList());

    ErrorMessage message = new ErrorMessage(
      HttpStatus.BAD_REQUEST.value(),
      new Date(),
      errors.toString(),
      request.getDescription(false));

    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex,
                                                      final HttpHeaders headers,
                                                      final HttpStatus status,
                                                      final WebRequest request) {


    ErrorMessage message = new ErrorMessage(
      HttpStatus.BAD_REQUEST.value(),
      new Date(),
      ex.getMessage(),
      request.getDescription(false));

    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
    ErrorMessage message = new ErrorMessage(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      new Date(),
      ex.getMessage(),
      request.getDescription(false));

    return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}