package org.example.message;

import org.json.JSONObject;

public class ServerMessage extends JsonMessage {
    private final ServerMessageCategory messageCategory;
    public ServerMessage(ServerMessageCategory serverMessageCategory,
                         JSONObject body) {
        super(body);
        this.messageCategory = serverMessageCategory;
    }

    @Override
    public JSONObject getHeader() {
        JSONObject header = super.getHeader();
        header.put("messageCategory", messageCategory);
        return header;
    }
}
