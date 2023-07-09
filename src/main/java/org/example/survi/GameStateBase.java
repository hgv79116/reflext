package org.example.survi;

import org.example.message.JsonMessage;
import org.json.JSONObject;

public abstract class GameStateBase implements TimedComponent {
    protected long startTime = -1;
    protected long lastTimeStamp = -1;
    protected long endTime = -1;

    public void start() {
        startTime = System.currentTimeMillis();
        lastTimeStamp = startTime;
    }

    public void end() {
        endTime = System.currentTimeMillis();
        lastTimeStamp = endTime;
    }

    public void update(long newTimeStamp) {
        lastTimeStamp = newTimeStamp;
    }
    public abstract JSONObject getLastState(); // I did not declare the method as an abstract method
    // so it cannot be overidden by subclass.
    public JSONObject getCurrentState() {
        update(System.currentTimeMillis());
        return getLastState();
    }

    public abstract void update(JsonMessage jsonMessage);

    public boolean hasStarted() {
        return startTime != -1;
    }

    public boolean hasEnded() {
        return endTime != -1;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }
}
