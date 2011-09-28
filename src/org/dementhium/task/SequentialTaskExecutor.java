package org.dementhium.task;

import java.util.Collection;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class SequentialTaskExecutor {

    public void performTasks(Collection<Task> tasks) {
        for (Task task : tasks) {
            try {
                task.execute();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
