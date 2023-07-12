package org.example.message.server_message.game_status_message;

import org.json.JSONObject;

public class GameEndedMessage extends GameStatusMessage{
    public GameEndedMessage(long timeStamp, String message) {
        super(timeStamp, GameStatusMessageCategory.GAME_ENDED, new JSONObject().put("message", message));
    }

    public GameEndedMessage(long timeStamp) {
        super(timeStamp, GameStatusMessageCategory.GAME_ENDED, null);
    }
}
