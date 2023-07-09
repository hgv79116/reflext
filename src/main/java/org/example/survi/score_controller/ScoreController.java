package org.example.survi.score_controller;

import org.example.survi.GameStateBase;

public abstract class ScoreController extends GameStateBase{
    protected double maxScore;
    protected double minScore;

    public ScoreController(double minScore, double maxScore) {
        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    public abstract double getScore();
}
