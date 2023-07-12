package org.example.survi.score_controller;

import org.example.message.JsonMessage;
import org.example.message.client_message.ClientMessage;
import org.json.JSONObject;

public class LinearSpeedScoreController extends ScoreController {
    private final double defaultScore;
    private final double defaultSpeed;
    private double score;
    private double speed;
    private int direction;
    private long lastTimeStamp;

    public LinearSpeedScoreController(double minScore,
                                      double maxScore,
                                      double defaultScore,
                                      double defaultSpeed,
                                      int initial_direction) {
        super(minScore, maxScore);
        this.defaultScore = defaultScore;
        this.defaultSpeed = defaultSpeed;
        this.score = defaultScore;
        assert (initial_direction == -1 || initial_direction == 1);
        this.direction = initial_direction;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public void update(long newTimeStamp) {
        long deltaTime = newTimeStamp - lastTimeStamp;
        lastTimeStamp = newTimeStamp;

        score += speed * direction * deltaTime;
        if(score >= maxScore) {
            score = maxScore;
            direction = -1;
        }
        if(score <= minScore) {
            score = minScore;
            direction = -1;
        }
    }

    public double getScore(long timeStamp) {
        assert (timeStamp >= lastTimeStamp);
        update(timeStamp);
        return score;
    }

    @Override
    public JSONObject getLastState() {
        JSONObject JSONContent = new JSONObject();
        JSONContent.put("minScore", minScore);
        JSONContent.put("maxScore", maxScore);
        JSONContent.put("score", score);
        return JSONContent;
    }
}
