package com.n26.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Snapshot {

  private final BigDecimal sum;
  private final BigDecimal max;
  private final BigDecimal min;
  private final long count;
  private final long timestamp;

  public Snapshot(
      BigDecimal sum,
      BigDecimal max,
      BigDecimal min,
      long count,
      long timestamp
  ) {
    this.sum = sum;
    this.max = max;
    this.min = min;
    this.count = count;
    this.timestamp = timestamp;
  }

  public BigDecimal getSum() {
    return sum;
  }

  public BigDecimal getMax() {
    return max;
  }

  public BigDecimal getMin() {
    return min;
  }

  public long getCount() {
    return count;
  }

  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "Snapshot{" +
        "sum=" + sum +
        ", max=" + max +
        ", min=" + min +
        ", count=" + count +
        ", timestamp=" + timestamp +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Snapshot)) {
      return false;
    }
    Snapshot snapshot = (Snapshot) o;
    return count == snapshot.count &&
        timestamp == snapshot.timestamp &&
        Objects.equals(sum, snapshot.sum) &&
        Objects.equals(max, snapshot.max) &&
        Objects.equals(min, snapshot.min);
  }

  @Override
  public int hashCode() {

    return Objects.hash(sum, max, min, count, timestamp);
  }
}
