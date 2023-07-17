package org.example.survi.player;

import org.example.message.client_message.ClientMessage;
import org.example.survi.GameStateBase;
import org.example.survi.IndexedComponent;
import org.example.survi.category.UserCategoryController;
import org.example.survi.score_controller.LinearSpeedScoreController;
import org.example.survi.score_controller.LinearSpeedScoreControllerFactory;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Player extends GameStateBase implements IndexedComponent {
    private final int playerId;
    private final int numCategory;
    private double score = 0;
    private final String username;
    private final LinearSpeedScoreControllerFactory linearSpeedScoreControllerFactory
            = new LinearSpeedScoreControllerFactory();
    private final ArrayList<UserCategoryController> userCategoryControllers;

    // constructors
    public Player(int playerId,
                  int numCategory,
                  JSONObject userInfo) {
        this.playerId = playerId;
        this.numCategory = numCategory;
        this.username = userInfo.getString("username");
        userCategoryControllers = new ArrayList<>();
        for(int i = 0; i  < numCategory; i++) {
            userCategoryControllers.add(new UserCategoryController(i, linearSpeedScoreControllerFactory.newInstance()));
        }
    }

    @Override
    public void start(long timeStamp) {
        super.start(timeStamp);

        for(UserCategoryController controller: userCategoryControllers) {
            controller.start(timeStamp);
        }
    }

    @Override
    public void end(long timeStamp) {
        super.end(timeStamp);

        for(UserCategoryController controller: userCategoryControllers) {
            controller.end(timeStamp);
        }
    }

    @Override
    public void update(long newTimeStamp) {
        super.update(newTimeStamp);

        System.out.println("userrrrrr");
        for(UserCategoryController userCategoryController: userCategoryControllers) {
            userCategoryController.update(newTimeStamp);
        }
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

        score += userCategoryControllers.get(category).updateAndGetScore(timeStamp);
    }
}
