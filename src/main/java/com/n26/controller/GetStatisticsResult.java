package com.n26.controller;

public class GetStatisticsResult {

  private String sum;
  private String avg;
  private String max;
  private String min;
  private long count;

  public GetStatisticsResult() {
  }

  public GetStatisticsResult(
      String sum,
      String avg,
      String max,
      String min,
      long count
  ) {
    this.sum = sum;
    this.avg = avg;
    this.max = max;
    this.min = min;
    this.count = count;
  }

  public String getSum() {
    return sum;
  }

  public String getAvg() {
    return avg;
  }

  public String getMax() {
    return max;
  }

  public String getMin() {
    return min;
  }

  public long getCount() {
    return count;
  }

  public void setSum(String sum) {
    this.sum = sum;
  }

  public void setAvg(String avg) {
    this.avg = avg;
  }

  public void setMax(String max) {
    this.max = max;
  }

  public void setMin(String min) {
    this.min = min;
  }

  public void setCount(long count) {
    this.count = count;
  }

  @Override
  public String toString() {
    return "GetStatisticsResult{" +
        "sum='" + sum + '\'' +
        ", avg='" + avg + '\'' +
        ", max='" + max + '\'' +
        ", min='" + min + '\'' +
        ", count=" + count +
        '}';
  }
}
