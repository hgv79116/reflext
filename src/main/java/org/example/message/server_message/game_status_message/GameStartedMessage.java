package org.example.message.server_message.game_status_message;

import org.json.JSONObject;

public class GameStartedMessage extends GameStatusMessage{
    public GameStartedMessage(long timeStamp, String message) {
        super(timeStamp, GameStatusMessageCategory.GAME_STARTED, new JSONObject().put("message", message));
    }

    public GameStartedMessage(long timeStamp) {
        super(timeStamp, GameStatusMessageCategory.GAME_STARTED, null);
    }
}
