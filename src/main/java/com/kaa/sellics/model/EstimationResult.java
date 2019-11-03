package com.kaa.sellics.model;

public class EstimationResult {
    private String keyword;
    private int score;

    public EstimationResult() {
    }

    public EstimationResult(String keyword, int score) {
        this.keyword = keyword;
        this.score = score;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
