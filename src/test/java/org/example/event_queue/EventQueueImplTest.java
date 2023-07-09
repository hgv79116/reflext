package org.example.event_queue;

import org.example.event.Event;
import org.example.event.EventImpl;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EventQueueImplTest {
    private Exception thrown = null;
    private Event getResult;

    @Test
    public void test() {
        Event a = new EventImpl(System.currentTimeMillis(), "a");
        Event b = new EventImpl(System.currentTimeMillis() + 10, "b");
        System.out.println("Running test: " + a.getTimeStamp() + " " + b.getTimeStamp());
        EventQueue eventQueue = new EventQueueImpl();
        eventQueue.addEvent(a);
        eventQueue.addEvent(b);
        try {
            Event c = eventQueue.getEvent();
            Assert.assertEquals(a.getTimeStamp(), c.getTimeStamp());
            System.out.println("Retrieved" + c.getTimeStamp());
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            Event d = eventQueue.getEvent();
            Assert.assertEquals(d.getTimeStamp(), b.getTimeStamp());
            System.out.println("Retrieved" + d.getTimeStamp());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

//    @Test
//    public void test1_1() throws InterruptedException {
//        EventQueue eventQueue = new EventQueueImpl();
//        try {
//            eventQueue.getEvent();
//        } catch (Exception e) {
//            throw e;
//        }
//    }

    // needs to have throws InterruptedException to work.
    @Test
    public void test2() throws InterruptedException {
        EventQueue eventQueue = new EventQueueImpl();
        thrown = null;
        Thread supplierThread = new Thread(() -> {
            try {
                eventQueue.getEvent();
            } catch (Exception e) {
                System.out.println(e);
                thrown = e; // thrown is on heap, not stack, so this is legit.
            }
        });
        supplierThread.start();
        supplierThread.interrupt();
        try {
            supplierThread.join();
            System.out.println(thrown); // this cause error, because
            // interrupt does not end supplierThread immediately. it simply
            // request the thread to stop at the first convinient time.
            Assert.assertTrue(thrown instanceof InterruptedException);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void test3() {
        EventQueue eventQueue = new EventQueueImpl();
        final Event addedEvent = new EventImpl(System.currentTimeMillis(), "no messsage");
        Thread supplierThread = new Thread(() -> {
            try {
                Thread.sleep(5000);
                System.out.println("sleeping");
                System.out.flush();
            } catch (Exception e) {
                System.out.println("failed to sleep");
            } finally {
                System.out.println("adding lol");
                eventQueue.addEvent(addedEvent);
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                getResult = eventQueue.getEvent();
            } catch (Exception e) {
                System.out.println("failed to get");
            }
        });

        supplierThread.start();
        consumerThread.start();

        try {
            supplierThread.join();
            consumerThread.join();
            Assert.assertEquals(addedEvent, getResult);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
