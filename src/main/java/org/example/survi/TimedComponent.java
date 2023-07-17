package org.example.survi;

public interface TimedComponent {
    void start(long timeStamp);
    void update(long newTimeStamp);
    void end(long timeStamp);
}
