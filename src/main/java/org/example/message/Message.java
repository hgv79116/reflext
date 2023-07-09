package org.example.message;

public class Message {
    private final long timeStamp;

    Message(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }
}
