package com.n26.controller;

public class CreateTransactionRequest {

  private String amount;
  private String timestamp;

  public CreateTransactionRequest() {
  }

  public CreateTransactionRequest(String amount, String timestamp) {
    this.amount = amount;
    this.timestamp = timestamp;
  }

  public String getAmount() {
    return amount;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "CreateTransactionRequest{" +
        "amount=" + amount +
        ", timestamp=" + timestamp +
        '}';
  }
}