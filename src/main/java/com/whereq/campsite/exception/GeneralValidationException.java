package com.whereq.campsite.exception;

public class GeneralValidationException extends RuntimeException {
  public GeneralValidationException(String exceptionMessage) {
    super(exceptionMessage);
  }
}
