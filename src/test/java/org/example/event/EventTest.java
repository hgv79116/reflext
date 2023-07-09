package org.example.event;

import org.junit.Assert;
import org.junit.Test;

public class EventTest {
    @Test
    public void test() {
        Event e = new EventImpl(System.currentTimeMillis(), "testEvent");
        Assert.assertNotEquals(e.getTimeStamp(), 0);
    }
}
