package org.example.message.server_message.game_status_message;

import org.example.message.server_message.ServerMessage;
import org.json.JSONObject;

public class GameStateMessage extends GameStatusMessage {
    public GameStateMessage(long timeStamp, JSONObject gameState) {
        super(timeStamp,
                GameStatusMessageCategory.GAME_STATE,
                new JSONObject().put("gameState", gameState));
    }
}
