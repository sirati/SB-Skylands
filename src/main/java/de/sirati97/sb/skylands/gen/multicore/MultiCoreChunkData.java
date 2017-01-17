package de.sirati97.sb.skylands.gen.multicore;

import de.sirati97.sb.skylands.nms.BetterChunkSnapshot;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

/**
 * Created by sirati97 on 12.06.2016 for sb-skylands.
 */
public class MultiCoreChunkData extends TimedCord2d {
    public volatile boolean awaitChunk = false;
    public volatile boolean finished = false;
    public volatile boolean invokedChunkLoad = false;
    public BetterChunkSnapshot chunkSnapshot;
    public final World world;
    public final Random random;
    public final long id;
    public final ChunkGenerator.BiomeGrid biomeGrid;

    public MultiCoreChunkData(World world, Random random, int x, int z, long id, ChunkGenerator.BiomeGrid biomeGrid) {
        super(x, z);
        this.world = world;
        this.random = random;
        this.id = id;
        this.biomeGrid = biomeGrid;
    }
}
