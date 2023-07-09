package org.example.message;

import org.json.JSONObject;

public abstract class JsonMessage extends Message{
    protected JSONObject body;
    public JsonMessage(JSONObject body) {
        super(System.currentTimeMillis());
        this.body = body;
    }
    public JsonMessage(long timeStamp, JSONObject body)  {
        super(timeStamp);
        this.body = body;
    }

    public JSONObject getHeader() {
        JSONObject header = new JSONObject();
        header.put("timeStamp", timeStamp);
        return header;
    }

    public JSONObject getBody() {
        return body;
    }

    public JSONObject toJSON() {
        JSONObject message = new JSONObject();
        message.put("header", getHeader());
        message.put("body", getBody());
        return message;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }
}
