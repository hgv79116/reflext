package org.example.message;

import org.json.JSONObject;

public class GameStateMessage extends ServerMessage {
    public GameStateMessage(JSONObject body) {
        super(ServerMessageCategory.GAME_STATUS, body);
    }
}
