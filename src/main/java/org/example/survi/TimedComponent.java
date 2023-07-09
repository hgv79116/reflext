package org.example.survi;

public interface TimedComponent {
    void start();
    void update(long newTimeStamp);
    void end();
}
