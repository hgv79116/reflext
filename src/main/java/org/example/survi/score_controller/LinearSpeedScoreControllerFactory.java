package org.example.survi.score_controller;

public class LinearSpeedScoreControllerFactory implements ScoreControllerFactory {
    private final double DEFAULT_SCORE = 0;
    private final double DEFAULT_SPEED = 0.001;
    private final double MIN_SCORE = -1;
    private final double MAX_SCORE = 1;
    private final int INITIAL_DIRECTION = 1;
    private double score;
    private double speed;
    private int direction;
    private long lastTimeStamp;

    public LinearSpeedScoreControllerFactory() {
    }

    public LinearSpeedScoreController newInstance() {
        return new LinearSpeedScoreController(MIN_SCORE, MAX_SCORE, DEFAULT_SCORE, DEFAULT_SPEED, INITIAL_DIRECTION);
    }
}
