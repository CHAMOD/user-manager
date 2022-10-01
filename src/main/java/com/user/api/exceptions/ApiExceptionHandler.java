
package com.user.api.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

//  @ExceptionHandler(MethodArgumentNotValidException.class)
//  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
//  @ResponseBody
//  public ApiError handleValidationError(MethodArgumentNotValidException ex) {
//    BindingResult bindingResult = ex.getBindingResult();
//    FieldError fieldError = bindingResult.getFieldError();
//    String defaultMessage = fieldError.getDefaultMessage();
//    return new ApiError("VALIDATION_FAILED", defaultMessage);
//  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex, HttpHeaders headers,
    HttpStatus status, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDate.now());
    body.put("status", status.value());

    List<String> errors = ex.getBindingResult()
      .getFieldErrors()
      .stream()
      .map(x -> x.getDefaultMessage())
      .collect(Collectors.toList());

    body.put("errors", errors);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }
}