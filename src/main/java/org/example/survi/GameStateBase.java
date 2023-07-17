package org.example.survi;

import org.example.message.JsonMessage;
import org.example.message.client_message.ClientMessage;
import org.json.JSONObject;

public abstract class GameStateBase implements TimedComponent {
    protected long startTime = -1;
    protected long lastTimeStamp = -1;
    protected long endTime = -1;

    public boolean hasStarted() {
        return startTime != -1;
    }

    public boolean hasEnded() {
        return endTime != -1;
    }

    public void start(long timeStamp) {
        assert(!hasEnded() && !hasStarted());
        startTime = timeStamp;
        lastTimeStamp = startTime;
    }

    public void end(long timeStamp) {
        assert (!hasEnded() && hasStarted() && timeStamp >= lastTimeStamp);
        endTime = timeStamp;
        lastTimeStamp = endTime;
    }

    // dependency map: update (message) -> update (time).
    // update & getState -> update (time), getLastState.
    // update -> end

    public void update(long newTimeStamp) {
        assert (hasStarted() && !hasEnded() && newTimeStamp >= lastTimeStamp);
        lastTimeStamp = newTimeStamp;
    }

    public abstract JSONObject getLastState();


    public JSONObject updateAndGetState(long newTimeStamp) { // I did not declare the method as an abstract method
        update(newTimeStamp);
        return getLastState();
    }

    // needs to be overridden
    public void update(ClientMessage clientMessage) {
        update(clientMessage.getTimeStamp());
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }
}
