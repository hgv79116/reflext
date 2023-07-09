package org.example.message;

import org.json.JSONObject;

public class ConnectionStatusMessage extends ServerMessage {
    public ConnectionStatusMessage(ConnectionStatus connectionStatus) {
        super(ServerMessageCategory.CONNECTION_STATUS,
                new JSONObject().put("connectionStatus", connectionStatus));
    }
}
