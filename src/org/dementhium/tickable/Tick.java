package org.dementhium.tickable;

/**
 * @author 'Mystic Flow
 */
public abstract class Tick {

    private int time;

    private int attempts;
    private boolean running = true;

    public Tick(int time) {
        this.time = time;
        this.attempts = 0;
    }

    public boolean run() {
        if (!running) {
            return false;
        }
        if (++attempts >= time) {
            attempts = 0;
            try {
                execute();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return running;
    }

    public boolean isRunning() {
        return running;
    }

    public void setTime(int cycles) {
        this.time = cycles;
    }

    public int getTime() {
        return time;
    }

    public void stop() {
        running = false;
    }

    public void start() {
        running = true;
    }

    public abstract void execute();

}
