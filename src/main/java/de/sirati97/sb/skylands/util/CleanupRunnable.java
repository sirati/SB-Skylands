package de.sirati97.sb.skylands.util;

import org.bukkit.scheduler.BukkitTask;

import java.lang.ref.WeakReference;

/**
 * Created by sirati97 on 13.06.2016.
 */
public class CleanupRunnable implements Runnable {
    private final WeakReference<Cleanable> cleanable;
    private BukkitTask task;

    public CleanupRunnable(Cleanable cleanable) {
        this.cleanable = new WeakReference<>(cleanable);
    }

    @Override
    public void run() {
        Cleanable cleanable = this.cleanable.get();
        if (cleanable == null) {
            task.cancel();
        } else {
            cleanable.cleanUp();
        }
    }

    public void setTask(BukkitTask task) {
        this.task = task;
    }
}
