package de.sirati97.sb.skylands.nms;

import net.minecraft.server.v1_11_R1.Block;
import net.minecraft.server.v1_11_R1.ChunkSnapshot;
import net.minecraft.server.v1_11_R1.IBlockData;

import java.lang.reflect.Field;

/**
 * Created by sirati97 on 12.06.2016.
 */
public class BetterChunkSnapshot extends ChunkSnapshot {
    private final char[] data;
    private final IBlockData air;

    public BetterChunkSnapshot() {
        try {
            Field dataField = ChunkSnapshot.class.getDeclaredField("b"); //CHANGE THAT WITH NEW VERSION
            dataField.setAccessible(true);
            data = (char[]) dataField.get(this);
            Field airField = ChunkSnapshot.class.getDeclaredField("a"); //CHANGE THAT WITH NEW VERSION
            airField.setAccessible(true);
            air = (IBlockData) airField.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }

    }

    private static int cordsToIndex(int x, int y, int z) {
        return x << 12 | z << 8 | y;
    }

    public IBlockData get(int index) {
        if(index >= 0 && index < data.length) {
            IBlockData result = Block.REGISTRY_ID.fromId(data[index]);
            return result != null?result: air;
        } else {
            throw new IndexOutOfBoundsException("The coordinate is out of range");
        }
    }

    public IBlockData get(int x, int y, int z) {
        return get(cordsToIndex(x, y, z));
    }

    public void set(int index, IBlockData blockData) {
        if(index >= 0 && index < data.length) {
            IBlockData result = Block.REGISTRY_ID.fromId(data[index]);
            data[index] = (char)Block.REGISTRY_ID.getId(blockData);
        } else {
            throw new IndexOutOfBoundsException("The coordinate is out of range");
        }
    }

    public void set(int x, int y, int z, IBlockData blockData) {
        set(cordsToIndex(x, y, z), blockData);
    }
}
