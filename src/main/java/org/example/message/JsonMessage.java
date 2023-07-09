package org.example.message;

import org.json.JSONObject;

public class JsonMessage extends Message{
    private JSONObject content;

    public JsonMessage(long timeStamp, JSONObject content)  {
        super(timeStamp);
        this.content = content;
    }

    public JSONObject getContent() {
        return content;
    }
}
