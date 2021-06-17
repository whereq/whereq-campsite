package com.whereq.campsite.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Just a simple sequence generator for single instance environment
 */
public class SequenceGenerator {

  private static final AtomicLong sequence = new AtomicLong(1);

  private SequenceGenerator() {
  }

  public static Long next() {
    return sequence.getAndIncrement();
  }

}
