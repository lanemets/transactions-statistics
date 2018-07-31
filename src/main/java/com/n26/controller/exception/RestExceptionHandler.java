package com.n26.controller.exception;


import com.n26.controller.exception.Exceptions.DateExceededException;
import com.n26.controller.exception.Exceptions.IncorrectAmountException;
import com.n26.controller.exception.Exceptions.IncorrectDateException;
import com.n26.controller.exception.Exceptions.OutdatedException;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({
      IncorrectAmountException.class, IncorrectDateException.class, DateExceededException.class
  })
  public ResponseEntity<?> handleNotParsableData() {
    return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(OutdatedException.class)
  public ResponseEntity<?> handleOutdated() {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleInternal() {
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @SuppressWarnings("unused")
  public ResponseEntity<Problem> handleException(HttpMessageNotReadableException e) {
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

}