package de.sirati97.sb.skylands.gen.multicore;

/**
 * Created by sirati97 on 13.06.2016.
 */
public class TimedCord2d extends Cord2d {
    public final long timestamp;


    public TimedCord2d(Cord2d cord) {
        this(cord.x, cord.z);
    }

    public TimedCord2d(int x, int z) {
        this(x, z, System.currentTimeMillis());
    }

    public TimedCord2d(int x, int z, long timestamp) {
        super(x, z);
        this.timestamp = timestamp;
    }
}
