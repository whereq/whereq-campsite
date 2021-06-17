package com.whereq.campsite.enums;

public enum ActionStatusEnum {
  Success("Success"),
  Fail("Fail");

  private final String actionStatus;


  ActionStatusEnum(String actionStatus) {
    this.actionStatus = actionStatus;
  }

  @Override
  public String toString() {
    return this.actionStatus;
  }
}
