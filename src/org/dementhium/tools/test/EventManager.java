package org.dementhium.tools.test;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class EventManager implements Runnable {

    public enum EventPriority {
        HIGH, MEDIUM, LOW
    }

    private BlockingQueue<Event> events = new PriorityBlockingQueue<Event>();
    private Executor executor = Executors.newSingleThreadExecutor();

    private volatile boolean running = true;

    public EventManager() {
        executor.execute(this);
    }

    public void submit(Event event) {
        events.add(event);
    }

    @Override
    public void run() {
        while (running) {
            Iterator<Event> it = events.iterator();

            long sleepTime = -1;

            while (it.hasNext()) {
                Event event = it.next();
                if (event.isReady()) { //check if we're ready to execute
                    event.run();
                } else {
                    if (sleepTime == -1 || sleepTime > event.getDelay()) {
                        sleepTime = event.getDelay();
                    }
                }
                if (!event.isRunning()) { //after execution we check if we're still running
                    it.remove();
                }
            }
            try {
                if (sleepTime == -1) { //No events in the queue
                    Event e = events.take(); //TODO Find a better way to block this thread
                    events.add(e);
                    continue;
                }
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void halt() {
        running = false;
    }

}
