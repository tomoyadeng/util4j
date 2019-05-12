package com.tomoyadeng.util4j.distributed;

import org.junit.Test;

public class SnowFlakeTest {
  @Test
  public void testSnowFlake() {
    SnowFlake snowFlake = new SnowFlake(1, 1);
    for (int i = 0; i < 10; i++) {
      System.out.println(snowFlake.nextId());
    }
  }
}
