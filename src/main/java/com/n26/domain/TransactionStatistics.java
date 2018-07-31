package com.n26.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransactionStatistics {

  public static final TransactionStatistics EMPTY = new TransactionStatistics(
      BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP),
      BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP),
      BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP),
      BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP),
      0L
  );

  private final BigDecimal sum;
  private final BigDecimal avg;
  private final BigDecimal max;
  private final BigDecimal min;
  private final long count;

  public TransactionStatistics(
      BigDecimal sum,
      BigDecimal avg,
      BigDecimal max,
      BigDecimal min,
      long count
  ) {
    this.sum = sum;
    this.avg = avg;
    this.max = max;
    this.min = min;
    this.count = count;
  }

  public BigDecimal getSum() {
    return sum;
  }

  public BigDecimal getAvg() {
    return avg;
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

  @Override
  public String toString() {
    return "TransactionStatistics{" +
        "sum=" + sum +
        ", avg=" + avg +
        ", max=" + max +
        ", min=" + min +
        ", count=" + count +
        '}';
  }
}
