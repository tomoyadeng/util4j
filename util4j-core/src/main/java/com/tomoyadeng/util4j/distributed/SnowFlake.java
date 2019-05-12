package com.tomoyadeng.util4j.distributed;

/**
 * Twitter SnowFlake, Java Implementation
 */
public class SnowFlake {
  // ==============================Fields===========================================
  private static final long EPOCH = 1420041600000L;

  private static final long WORKER_ID_BITS = 5L;

  private static final long DATA_CENTER_ID_BITS = 5L;

  private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

  private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);

  private static final long SEQUENCE_BITS = 12L;

  private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

  private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

  private static final long TIMESTAMP_LEFT_SHIFT =
      SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

  private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

  private long workerId;

  private long dataCenterId;

  private long sequence = 0L;

  private long lastTimestamp = -1L;

  // ==============================Constructors=====================================
  public SnowFlake(long workerId, long dataCenterId) {
    if (workerId > MAX_WORKER_ID || workerId < 0) {
      throw new IllegalArgumentException(
          String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
    }
    if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
      throw new IllegalArgumentException(
          String.format(
              "data center Id can't be greater than %d or less than 0", MAX_DATA_CENTER_ID));
    }
    this.workerId = workerId;
    this.dataCenterId = dataCenterId;
  }

  public synchronized long nextId() {
    long timestamp = timeGen();

    if (timestamp < lastTimestamp) {
      throw new RuntimeException(
          String.format(
              "Clock moved backwards.  Refusing to generate id for %d milliseconds",
              lastTimestamp - timestamp));
    }

    if (lastTimestamp == timestamp) {
      sequence = (sequence + 1) & SEQUENCE_MASK;
      if (sequence == 0) {
        timestamp = tilNextMillis(lastTimestamp);
      }
    }
    else {
      sequence = 0L;
    }

    lastTimestamp = timestamp;

    return ((timestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT)
        | (dataCenterId << DATA_CENTER_ID_SHIFT)
        | (workerId << WORKER_ID_SHIFT)
        | sequence;
  }

  private long tilNextMillis(long lastTimestamp) {
    long timestamp = timeGen();
    while (timestamp <= lastTimestamp) {
      timestamp = timeGen();
    }
    return timestamp;
  }

  private long timeGen() {
    return System.currentTimeMillis();
  }
}
