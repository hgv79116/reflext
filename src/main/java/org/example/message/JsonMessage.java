package org.example.message;

import org.json.JSONObject;

public class JsonMessage extends Message{
    private JSONObject content;
    public JsonMessage(JSONObject content) {
        super(System.currentTimeMillis());
        this.content = content;
    }
    public JsonMessage(long timeStamp, JSONObject content)  {
        super(timeStamp);
        this.content = content;
    }

    public JSONObject getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content.toString();
    }
}
