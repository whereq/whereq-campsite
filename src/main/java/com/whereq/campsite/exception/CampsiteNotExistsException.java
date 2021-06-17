package com.whereq.campsite.exception;

public class CampsiteNotExistsException extends RuntimeException {
  public CampsiteNotExistsException(String exceptionMessage) {
    super(exceptionMessage);
  }
}
