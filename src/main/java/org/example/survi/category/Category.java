package org.example.survi.category;

import org.example.message.JsonMessage;
import org.example.survi.GameStateBase;
import org.example.survi.IndexedComponent;
import org.example.survi.score_controller.ScoreController;
import org.json.JSONObject;

public class Category extends GameStateBase implements IndexedComponent {
    final int id;
    final int code;
    ScoreController scoreController;

    public Category(int id,
                    int code,
                    ScoreController scoreController) {
        this.id = id;
        this.code = code;
        this.scoreController = scoreController;
    }

    public void setScoreController(ScoreController scoreController) {
        this.scoreController = scoreController;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void start() {
        super.start();
        scoreController.start();
    }

    @Override
    public void end() {
        super.end();
        scoreController.end();
    }

    @Override
    public void update(JsonMessage jsonMessage) {

    }

    @Override
    public void update(long newTimeStamp) {
        super.update(newTimeStamp);
        scoreController.update(newTimeStamp);
    }

    @Override
    public JSONObject getLastState() {
        JSONObject JSONContent = new JSONObject();
        JSONContent.put("id", id);
        JSONContent.put("code", code);
        JSONContent.put("score", scoreController.getLastState());
        return JSONContent;
    }
}
