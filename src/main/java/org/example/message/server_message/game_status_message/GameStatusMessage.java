package org.example.message.server_message.game_status_message;

import org.example.message.server_message.ServerMessage;
import org.example.message.server_message.ServerMessageCategory;
import org.json.JSONObject;

public class GameStatusMessage extends ServerMessage {
    public GameStatusMessage(long timeStamp,
                             GameStatusMessageCategory gameStatusMessageCategory,
                             JSONObject body) {
        super(timeStamp, ServerMessageCategory.GAME_STATUS, body);
        this.header.put("gameStatusMessageCategory", gameStatusMessageCategory);
    }
}
