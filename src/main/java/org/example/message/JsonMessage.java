package org.example.message;

import org.json.JSONObject;

public abstract class JsonMessage extends Message{
    protected JSONObject header;
    protected JSONObject body;
    public JsonMessage(long timeStamp, JSONObject body)  {
        super(timeStamp);
        this.header = new JSONObject();
        this.header.put("timeStamp", timeStamp);
        this.body = body;
    }

    public JsonMessage(JSONObject message) {
        this.header = message.getJSONObject("header");
        this.body = message.getJSONObject("body");
        assert (header != null && body != null);
    }

    public JSONObject getHeader() {
        return header;
    }

    public JSONObject getBody() {
        return body;
    }

    public JSONObject toJSON() {
        JSONObject message = new JSONObject();
        message.put("header", header);
        message.put("body", body);
        return message;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }
}
