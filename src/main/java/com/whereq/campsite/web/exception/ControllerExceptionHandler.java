package com.whereq.campsite.web.exception;

import com.whereq.campsite.exception.CampsiteNotExistsException;
import com.whereq.campsite.exception.CampsiteReservationException;
import com.whereq.campsite.exception.GeneralValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  private String wrapExceptionMessage(String message) {
    return "Don't be panic with the exception \n" + message + "\nJust stay calm :)";
  }
  @ExceptionHandler(value = {GeneralValidationException.class})
  protected ResponseEntity<Object> handleGeneralValidationException(GeneralValidationException ex, WebRequest webRequest) {
    return handleExceptionInternal(ex, wrapExceptionMessage(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
  }

  @ExceptionHandler(value = {CampsiteNotExistsException.class})
  protected ResponseEntity<Object> handleCampsiteNotExistsException(CampsiteNotExistsException ex, WebRequest webRequest) {
    return handleExceptionInternal(ex, wrapExceptionMessage(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);
  }

  @ExceptionHandler(value = {CampsiteReservationException.class})
  protected ResponseEntity<Object> handleCampsiteReservationException(CampsiteReservationException ex, WebRequest webRequest) {
    return handleExceptionInternal(ex, wrapExceptionMessage(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
  }
}
