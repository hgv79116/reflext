package org.example.message;

public class Message {
    protected final long timeStamp;

    Message(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    Message() {
        this.timeStamp = System.currentTimeMillis();
    }

    public long getTimeStamp() {

        return this.timeStamp;
    }
}
