package de.sirati97.sb.skylands.gen.multicore;

import de.sirati97.sb.skylands.nms.BetterChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.List;
import java.util.Random;

/**
 * Created by sirati97 on 12.06.2016.
 */
public interface IAsyncGenerator {
    ChunkGenerator.ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, ChunkGenerator.BiomeGrid biomeGrid);
    ChunkGenerator.ChunkData toChunkData(World world, Random random, int chunkX, int chunkZ, ChunkGenerator.BiomeGrid biomeGrid, BetterChunkSnapshot chunkSnapshot);
    BetterChunkSnapshot generateChunkDataAsync(World world, Random random, int chunkX, int chunkZ, ChunkGenerator.BiomeGrid biomeGrid);
    List<BlockPopulator> getDefaultPopulators(World world);
    Location getFixedSpawnLocation(World world, Random random);
}
