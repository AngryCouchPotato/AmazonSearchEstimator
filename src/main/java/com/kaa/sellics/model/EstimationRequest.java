package com.kaa.sellics.model;


import org.apache.commons.lang3.time.StopWatch;

public class EstimationRequest {
  private String keyword;
  private StopWatch timer;

  public EstimationRequest(String keyword, StopWatch timer) {
    this.keyword = keyword;
    this.timer = timer;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public StopWatch getTimer() {
    return timer;
  }

  public void setTimer(StopWatch timer) {
    this.timer = timer;
  }
}
