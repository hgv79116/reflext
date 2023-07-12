package org.example.message.server_message.connection_status_message;

import org.example.message.server_message.ServerMessage;
import org.example.message.server_message.ServerMessageCategory;
import org.json.JSONObject;

public class ConnectionStatusMessage extends ServerMessage {
    public ConnectionStatusMessage(long timeStamp,
                                   ConnectionStatusMessage connectionStatusMessage) {
        super(timeStamp,
                ServerMessageCategory.CONNECTION_STATUS,
                null);
    }
    public ConnectionStatusMessage(long timeStamp,
                                   ConnectionStatusCategory connectionStatus,
                                   String message) {
        super(timeStamp,
                ServerMessageCategory.CONNECTION_STATUS,
                new JSONObject().put("message", message));
    }
}
