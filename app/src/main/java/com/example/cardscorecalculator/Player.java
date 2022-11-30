package com.example.cardscorecalculator;

public class Player {
    private String name;
    private int score;
    private boolean host;


    public Player(){};
    public Player(String name, int score, boolean host){
        this.name = name;
        this.score = score;
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }
}
