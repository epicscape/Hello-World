package org.dementhium.task.runnable;

import org.dementhium.task.Task;

import java.util.concurrent.CountDownLatch;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class CountdownTask implements Runnable {

    private Task task;

    private CountDownLatch latch;

    public CountdownTask(Task task, CountDownLatch latch) {
        this.task = task;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            task.execute();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }
}
