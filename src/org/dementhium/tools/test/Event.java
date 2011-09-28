package org.dementhium.tools.test;

import org.dementhium.tools.test.EventManager.EventPriority;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public abstract class Event implements Comparable<Event> {

    private boolean running = true;
    private long delay;
    private long lastRun;

    private EventPriority priority;

    public Event(long delay, EventPriority priority) {
        this.delay = delay;
        this.lastRun = System.currentTimeMillis();
        this.priority = priority;
    }

    public boolean isReady() {
        return System.currentTimeMillis() - lastRun >= delay;
    }

    public void run() {
        execute();
        lastRun = System.currentTimeMillis();
    }

    public long getDelay() {
        return delay;
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }

    public int compareTo(Event other) {
        return priority.compareTo(other.priority);
    }

    public abstract void execute();

}
