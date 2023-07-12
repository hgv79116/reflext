package org.example.message.server_message.connection_status_message;

import org.example.message.server_message.ServerMessage;
import org.example.message.server_message.ServerMessageCategory;
import org.json.JSONObject;

public class InGameMessage extends ServerMessage {
    public InGameMessage(long timeStamp, String message) {
        super(timeStamp,
                ServerMessageCategory.CONNECTION_STATUS,
                new JSONObject().put("message", message));
        this.header.put("connectionStatusCategory", ConnectionStatusCategory.IN_GAME);
    }
}
