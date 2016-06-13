package de.sirati97.sb.skylands.gen.multicore;

/**
 * Created by sirati97 on 13.06.2016.
 */
public class Cord2d {
    public final int x;
    public final int z;

    public Cord2d(int x, int z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public int hashCode() {
        return x ^ ~z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cord2d) {
            Cord2d other = (Cord2d) obj;
            return other.x == x && other.z == z;
        }
        return false;
    }


    public static long getId(Cord2d cord2d) {
        return getId(cord2d.x, cord2d.z);
    }

    public static long getId(int x , int z) {
        return (long)x << 32 | z & 0xFFFFFFFFL;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{x="+x+",z="+z+"}";
    }
}
