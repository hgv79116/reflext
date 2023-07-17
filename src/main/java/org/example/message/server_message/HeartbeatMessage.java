package org.example.message.server_message;

import org.json.JSONObject;

public class HeartbeatMessage extends ServerMessage {
    public HeartbeatMessage(long timeStamp) {
        super(
                timeStamp,
                ServerMessageCategory.HEARTBEAT,
                new JSONObject().put("message", "Knock knock")
        );
    }
}
