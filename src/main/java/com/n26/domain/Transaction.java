package com.n26.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Transaction {

  private final BigDecimal amount;
  private final long timestamp;

  public Transaction(BigDecimal amount, long timestamp) {
    this.amount = amount;
    this.timestamp = timestamp;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "Transaction{" +
        "amount=" + amount +
        ", timestamp=" + timestamp +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Transaction)) {
      return false;
    }
    Transaction that = (Transaction) o;
    return timestamp == that.timestamp &&
        Objects.equals(amount, that.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, timestamp);
  }
}
