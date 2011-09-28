package org.dementhium.task;

import org.dementhium.task.runnable.CountdownTask;
import org.dementhium.util.DementhiumThreadFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 'Mystic Flow
 */
public class ParallelTaskExecutor {

    private ExecutorService parallelExecutor;

    public ParallelTaskExecutor() {
        parallelExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new DementhiumThreadFactory("ParallelTaskExecutor"));
    }

    public void performTasks(Collection<Task> tasks) {
        if (!tasks.isEmpty()) {
            tasks = Collections.unmodifiableCollection(tasks);

            CountDownLatch latch = new CountDownLatch(tasks.size());
            for (Task task : tasks) {
                parallelExecutor.submit(new CountdownTask(task, latch));
            }
            try {
                latch.await(600, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
