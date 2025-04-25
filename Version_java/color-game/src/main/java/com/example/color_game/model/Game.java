package com.example.colorgame.model;
public class Game {
    private int score;
    private int timeleft;
    private String difficulty;
    public Game(int score, int timeleft, String difficulty) {
        this.score = score;
        this.timeleft = timeleft;
        this.difficulty = difficulty;
    }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getTimeleft() { return timeleft; }
    public void setTimeleft(int timeleft) { this.timeleft = timeleft; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}