package org.example.survi.category;

import org.example.survi.GameStateBase;
import org.example.survi.IndexedComponent;
import org.example.survi.score_controller.ScoreController;
import org.json.JSONObject;

public class UserCategoryController extends GameStateBase implements IndexedComponent {
    final int category;
    ScoreController scoreController;

    public UserCategoryController(int category,
                    ScoreController scoreController) {
        this.category = category;
        this.scoreController = scoreController;
    }

    @Override
    public int getId() {
        return category;
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
    public void update(long newTimeStamp) {
        super.update(newTimeStamp);
        scoreController.update(newTimeStamp);
    }

    @Override
    public JSONObject getLastState() {
        JSONObject JSONContent = new JSONObject();
        JSONContent.put("id", category);
        JSONContent.put("score", scoreController.getLastState());
        return JSONContent;
    }

    public double getScore(long timeStamp) {
        update(timeStamp);
        return scoreController.getScore(timeStamp);
    }
}
