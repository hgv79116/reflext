package org.example.message.client_message;

import org.example.message.JsonMessage;
import org.json.JSONObject;

public class ClientMessage extends JsonMessage {
    public ClientMessage(JSONObject message) {
        super(message);
    }
    public ClientMessage(String s) {
        this(new JSONObject(s));
    }
}
