package org.example.survi.score_controller;

import org.example.message.JsonMessage;
import org.example.message.client_message.ClientMessage;
import org.json.JSONObject;

import java.util.Random;

public class LinearSpeedScoreController extends ScoreController {
    private double score;
    private final double speed;
    private int direction;
    private long lastTimeStamp;

    public LinearSpeedScoreController(double minScore,
                                      double maxScore,
                                      double defaultScore,
                                      double defaultSpeed,
                                      int initial_direction) {
        super(minScore, maxScore);
        this.score = defaultScore;
        this.speed = new Random().nextDouble() * defaultSpeed;
        assert (initial_direction == -1 || initial_direction == 1);
        this.direction = initial_direction;
    }

    @Override
    public void update(long newTimeStamp) {
        super.update(newTimeStamp);

        long deltaTime = newTimeStamp - lastTimeStamp;
        lastTimeStamp = newTimeStamp;

        score += speed * direction * deltaTime;
        if(score >= maxScore) {
            score = maxScore;
            direction = -1;
        }
        if(score <= minScore) {
            score = minScore;
            direction = 1;
        }
    }

    public double updateAndGetScore(long timeStamp) {
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
