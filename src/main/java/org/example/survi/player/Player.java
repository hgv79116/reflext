package org.example.survi.player;

import org.example.message.client_message.ClientMessage;
import org.example.survi.GameStateBase;
import org.example.survi.IndexedComponent;
import org.example.survi.category.UserCategoryController;
import org.json.JSONObject;

import java.util.ArrayList;

public class Player extends GameStateBase implements IndexedComponent {
    private final int playerId;
    private final int numCategory;
    private double score = 0;
    private ArrayList<UserCategoryController> userCategoryControllers;

    // constructors
    public Player(int playerId,
                  int numCategory) {
        this.playerId = playerId;
        this.numCategory = numCategory;
    }
    public int getPlayerId() {
        return this.playerId;
    }

    private void hit(int category, long timeStamp) {
        lastTimeStamp = timeStamp;
        score += userCategoryControllers.get(category).get
    }

    @Override
    public void update(long newTimeStamp) {
        super.update(newTimeStamp);
        for(UserCategoryController userCategoryController: userCategoryControllers) {
            userCategoryController.update(newTimeStamp);
        }
    }

    @Override
    public void update(ClientMessage clientMessage) {
        super.update(clientMessage);
    }

    @Override
    public JSONObject getLastState() {
        JSONObject content = new JSONObject();
        content.put("score", score);
        JSONObject categories = new JSONObject();
        for(UserCategoryController userCategoryController: userCategoryControllers) {
            categories.put(String.valueOf(userCategoryController.getId()), userCategoryController.getLastState());
        }
        content.put("categories", categories);
        return content;
    }

    @Override
    public int getId() {
        return playerId;
    }

    public void hit(long timeStamp, int category) {
        update(timeStamp);

        score += userCategoryControllers.get(category).getScore(timeStamp);
    }
}
