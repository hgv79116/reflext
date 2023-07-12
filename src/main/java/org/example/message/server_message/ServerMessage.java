package org.example.message.server_message;

import org.example.message.JsonMessage;
import org.json.JSONObject;

public class ServerMessage extends JsonMessage {
    public ServerMessage(long timeStamp,
                         ServerMessageCategory serverMessageCategory,
                         JSONObject body) {
        super(timeStamp, body);
        header.put("serverMessageCategory", serverMessageCategory);
    }
}
