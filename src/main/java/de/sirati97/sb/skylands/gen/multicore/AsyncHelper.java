package de.sirati97.sb.skylands.gen.multicore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sirati97 on 13.06.2016.
 */
public class AsyncHelper {
    private final static int MAX_THREADS = 4;
    private final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
    private volatile int active = 0;

    public synchronized void doTask(final MultiCoreGenerator generator) {
        if (this.active<0) {
            active = 0;
        }
        final int newTasks = this.active;
        for (int i = 0; i < (MAX_THREADS - newTasks); i++) {
            active++;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    generator.generateAsync();
                    active--;
                }
            });
        }
    }
}
