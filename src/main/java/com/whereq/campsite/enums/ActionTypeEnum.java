package com.whereq.campsite.enums;

public enum ActionTypeEnum {

  Book("Book"),
  Modify("Modify"),
  Cancel("Cancel");

  private final String actionType;


  ActionTypeEnum(String actionType) {
    this.actionType = actionType;
  }

  @Override
  public String toString() {
    return this.actionType;
  }
}
