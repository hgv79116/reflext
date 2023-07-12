package org.example.survi.score_controller;

public class LinearSpeedScoreControllerFactory implements ScoreControllerFactory {
    private final double defaultScore;
    private final double defaultSpeed;
    private final double minScore;
    private final double maxScore;
    private final int initial_direction;
    private double score;
    private double speed;
    private int direction;
    private long lastTimeStamp;

    public LinearSpeedScoreControllerFactory(double defaultScore,
                                             double defaultSpeed,
                                             double minScore,
                                             double maxScore,
                                             int initial_direction) {
        this.defaultScore = defaultScore;
        this.minScore = minScore;
        this.defaultSpeed = defaultSpeed;
        this.maxScore = maxScore;
        this.initial_direction = initial_direction;
    }

    public LinearSpeedScoreController newInstance() {
        return new LinearSpeedScoreController(minScore, maxScore, defaultScore, defaultSpeed, initial_direction);
    }
}
